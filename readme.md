# Jukebox Hopper mod

This is a mod which allows the player to use a hopper
to insert a record into a Jukebox in Minecraft.  

## Building

1. Copy the directory into your [MCP](https://minecraft.gamepedia.com/Programs_and_editors/Mod_Coder_Pack) workspace.
2. Locate method `transferItemsOut()` in `net.minecraft.tileentity`.
3. Add the snippet below at the start of the method.

Snippet:
```java
if(JukeboxHopperMod.canTransferRecord(this)){
    JukeboxHopperMod.transferRecord(this);
    return true;
}
```

You should be able to reobfuscate/startclient.  
Tested with Minecraft 1.12, if you are using a newer version you may
need to change a few things before it can run.  

