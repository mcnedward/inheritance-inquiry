package com.mcnedward.app.ui.listener;

import com.mcnedward.ii.service.graph.jung.JungGraph;

import java.util.Collection;

/**
 * Created by Edward on 10/1/2016.
 */
public interface GraphRequestListener {

    Collection<JungGraph> requestGraphs();
}
