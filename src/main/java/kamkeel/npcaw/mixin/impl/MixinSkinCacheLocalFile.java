package kamkeel.npcaw.mixin.impl;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import riskyken.armourersWorkshop.api.common.library.ILibraryFile;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinIdentifier;
import riskyken.armourersWorkshop.common.data.BidirectionalHashMap;
import riskyken.armourersWorkshop.common.skin.cache.SkinCacheLocalDatabase;
import riskyken.armourersWorkshop.common.skin.cache.SkinCacheLocalFile;
import riskyken.armourersWorkshop.common.skin.cache.SkinRequestMessage;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinIdentifier;
import riskyken.armourersWorkshop.utils.ModLogger;
import riskyken.armourersWorkshop.utils.SkinIOUtils;

import java.util.ArrayList;

@Mixin(value = SkinCacheLocalFile.class, remap = false)
public abstract class MixinSkinCacheLocalFile {

    // Shadowed fields from the original SkinCacheLocalFile class
    @Final
    @Shadow
    private Object cacheMapLock; // Lock for synchronizing access to cacheMapFileLink

    @Final
    @Shadow
    private BidirectionalHashMap<ILibraryFile, Integer> cacheMapFileLink; // Maps library files to skin IDs

    @Final
    @Shadow
    private ArrayList<SkinRequestMessage> skinLoadQueue; // Queue for skin load requests during soft loads

    @Final
    @Shadow
    private Object skinLoadQueueLock; // Lock for synchronizing access to skinLoadQueue

    @Final
    @Shadow
    private SkinCacheLocalDatabase cacheLocalDatabase; // Database for storing and retrieving skins

    @Shadow
    protected abstract Skin load(ISkinIdentifier skinIdentifier); // Abstract method to load a skin from file

    /**
     * Injects into the get method of SkinCacheLocalFile to optimize locking and avoid deadlocks.
     * @param requestMessage The request containing the skin identifier and optional player info
     * @param softLoad If true, queue the request instead of loading synchronously when not cached
     * @param cir Callback info to set the return value and cancel the original method
     */
    @Inject(method = "get(Lriskyken/armourersWorkshop/common/skin/cache/SkinRequestMessage;Z)Lriskyken/armourersWorkshop/common/skin/data/Skin;",
        at = @At("HEAD"),
        cancellable = true)
    private void onGet(SkinRequestMessage requestMessage, boolean softLoad, CallbackInfoReturnable<Skin> cir) {
        // Extract the skin identifier from the request message
        ISkinIdentifier identifier = requestMessage.getSkinIdentifier();

        // Variable to store the skin ID from the cache map
        Integer skinId;

        // Step 1: Check the cache map with minimal locking
        synchronized (cacheMapLock) {
            // Look up the skin ID for the library file in the cache map
            skinId = cacheMapFileLink.get(identifier.getSkinLibraryFile());

            // If the skin isn't cached and this is a soft load, queue it and exit early
            if (skinId == null && softLoad) {
                synchronized (skinLoadQueueLock) {
                    // Add the request to the queue for later processing
                    skinLoadQueue.add(requestMessage);
                }
                // Set return value to null (no skin available yet) and cancel original method
                cir.setReturnValue(null);
                return;
            }
        }

        // Step 2: Handle cases where we have a skin ID (skin is cached)
        if (skinId != null) {
            // Create a new identifier with the cached skin ID
            SkinIdentifier newIdentifier = new SkinIdentifier(skinId, identifier.getSkinLibraryFile(), 0, identifier.getSkinType());

            // Retrieve the skin from the database without holding cacheMapLock
            Skin skin = cacheLocalDatabase.get(newIdentifier, false);

            if (skin != null) {
                // If the skin is found, return it
                cir.setReturnValue(skin);
            } else {
                // If the skin should be in the cache but isn't, log a warning and return null
                ModLogger.log(Level.WARN, "Somehow failed to load a skin that we should have. ID was " + skinId);
                cir.setReturnValue(null);
            }
        }
        // Step 3: Handle hard load (synchronous load) when skin isn't cached
        else if (!softLoad) {
            // Load the skin synchronously outside of any lock
            Skin skin = load(identifier);

            if (skin != null) {
                // If loading succeeds, return the skin
                cir.setReturnValue(skin);
            } else {
                // If loading fails, return null (error logging could be added here)
                cir.setReturnValue(null);
            }
        }
        // Step 4: Soft load case where skin isn't cached (already queued in Step 1)
        else {
            cir.setReturnValue(null); // Return null as the request is already queued
        }
    }
}
