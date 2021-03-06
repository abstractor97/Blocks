package com.rvandoosselaer.blocks.examples;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.rvandoosselaer.blocks.Block;
import com.rvandoosselaer.blocks.BlockRegistry;
import com.rvandoosselaer.blocks.BlocksConfig;
import com.rvandoosselaer.blocks.Chunk;
import com.rvandoosselaer.blocks.ChunkMeshGenerator;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.mathd.Vec3i;
import com.simsilica.util.LogAdapter;

/**
 * An application that renders all the available blocks.
 *
 * Default key mappings:
 * print camera position:            c
 * print direct memory information:  m
 * toggle wireframe:                 p
 * toggle profiler:                  F6
 *
 * @author rvandoosselaer
 */
public class DefaultBlocks extends SimpleApplication {

    public static void main(String[] args) {
        LogAdapter.initialize();

        DefaultBlocks defaultBlocks = new DefaultBlocks();
        defaultBlocks.start();
    }

    public DefaultBlocks() {
        super(new StatsAppState(),
                new FlyCamAppState(),
                new DebugKeysAppState(),
                new LightingState(),
                new WireframeState(),
                new PostProcessingState(),
                new BasicProfilerState(false),
                new MemoryDebugState());
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        BlocksConfig.initialize(assetManager);

        BlockRegistry blockRegistry = BlocksConfig.getInstance().getBlockRegistry();
        BlocksConfig.getInstance().setChunkSize(new Vec3i(blockRegistry.getAll().size() * 2, 1, blockRegistry.getAll().size() * 2));

        Chunk chunk = Chunk.createAt(new Vec3i(0, 0, 0));

        Vec3i blockLocation = new Vec3i(0, 0, 0);
        for (Block block : blockRegistry.getAll()) {
            chunk.addBlock(blockLocation, block);
            blockLocation = blockLocation.add(2, 0, 0);
        }
        chunk.update();

        ChunkMeshGenerator meshGenerator = BlocksConfig.getInstance().getChunkMeshGenerator();
        chunk.createNode(meshGenerator);

        rootNode.attachChild(chunk.getNode());

        hideCursor();

        viewPort.setBackgroundColor(new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f));
        flyCam.setMoveSpeed(10f);
        cam.setLocation(new Vector3f(-3.5f, 3.5f, 6.5f));
        cam.lookAt(new Vector3f(BlocksConfig.getInstance().getChunkSize().x * 0.1f, 0, 0), Vector3f.UNIT_Y);
    }

    private void hideCursor() {
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        inputManager.setCursorVisible(false);
    }

}
