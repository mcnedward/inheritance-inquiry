package com.mcnedward.app.ui.component;

import com.mcnedward.ii.service.metric.element.Metric;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Edward on 10/1/2016.
 */
public class IIListModel<T extends Metric> extends AbstractListModel<T> {

    public static final int ALPHA = 1;
    public static final int VALUE = 2;
    private static final int A_TO_Z = 1;
    private static final int Z_TO_A = 2;
    private static final int LOW_TO_HIGH = 3;
    private static final int HIGH_TO_LOW = 4;

    private int mCurrentSort;
    private List<T> mMetrics;
    private Comparator<Metric> mAToZComparator;
    private Comparator<Metric> mZToAComparator;
    private Comparator<Metric> mLowToHighComparator;
    private Comparator<Metric> mHighToLowComparator;

    public IIListModel() {
        this(new ArrayList<>());
    }

    public IIListModel(List<T> metrics){
        mMetrics = metrics;
        mAToZComparator = new MetricAlphaAToZComparator();
        mZToAComparator = new MetricAlphaZToAComparator();
        mLowToHighComparator = new MetricValueLowToHighComparator();
        mHighToLowComparator = new MetricValueHighToLowComparator();
    }

    public int getSize(){
        return mMetrics.size();
    }

    public void addElement(T metric) {
        mMetrics.add(metric);
        int index = mMetrics.size() - 1;
        fireIntervalAdded(this, index, index);
    }

    public T getElementAt(int index){
        return mMetrics.get(index);
    }

    public void sort(int sortType) {
        if (sortType == ALPHA) {
            if (mCurrentSort == Z_TO_A)
                mCurrentSort = A_TO_Z;
            else
                mCurrentSort = Z_TO_A;
        }
        if (sortType == VALUE) {
            if (mCurrentSort == HIGH_TO_LOW)
                mCurrentSort = LOW_TO_HIGH;
            else
                mCurrentSort = HIGH_TO_LOW;
        }
        switch (mCurrentSort) {
            case A_TO_Z:
                sort(mAToZComparator);
                break;
            case Z_TO_A:
                sort(mZToAComparator);
                break;
            case LOW_TO_HIGH:
                sort(mLowToHighComparator);
                break;
            case HIGH_TO_LOW:
                sort(mHighToLowComparator);
                break;
        }
    }

    private void sort(Comparator<Metric> comparator) {
        Collections.sort(mMetrics, comparator);
        fireContentsChanged(this, 0, mMetrics.size());
    }

    public void clear() {
        mMetrics = new ArrayList<>();
    }

    public int size() {
        return mMetrics.size();
    }
}

class MetricAlphaAToZComparator implements Comparator<Metric> {
    @Override
    public int compare(Metric o1, Metric o2) {
        String metric1 = o1.getElementName();
        String metric2 = o2.getElementName();
        if (metric1 == metric2)
            return 0;
        if (metric1 == null)
            return -1;
        if (metric2 == null)
            return 1;
        return metric1.compareTo(metric2);
    }
}

class MetricAlphaZToAComparator implements Comparator<Metric> {
    @Override
    public int compare(Metric o1, Metric o2) {
        String metric1 = o1.getElementName();
        String metric2 = o2.getElementName();
        if (metric1 == metric2)
            return 0;
        if (metric2 == null)
            return -1;
        if (metric1 == null)
            return 1;
        return metric2.compareTo(metric1);
    }
}

class MetricValueLowToHighComparator implements Comparator<Metric> {
    @Override
    public int compare(Metric o1, Metric o2) {
        return o1.getMetric() - o2.getMetric();
    }
}

class MetricValueHighToLowComparator implements Comparator<Metric> {
    @Override
    public int compare(Metric o1, Metric o2) {
        return o2.getMetric() - o1.getMetric();
    }
}