package com.mcnedward.app.ui.listener;

import com.mcnedward.ii.service.graph.jung.JungGraph;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Edward on 9/26/2016.
 */
public interface GraphPanelListener {
    void onGraphsLoaded(JungGraph firstGraph);

    /**
     * Requests the JungGraphs. The key for the Map is the element's fully qualified name.
     * @param graphMap
     * @param downloadAll
     * @return
     */
    Collection<JungGraph> requestGraphs(Map<String, JungGraph> graphMap, boolean downloadAll);
}
