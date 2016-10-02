package com.mcnedward.app.ui.dialog.results;

import java.io.File;

/**
 * Created by Edward on 10/2/2016.
 */
public class ExportMetricFileResults {

    private File mDirectory;
    private boolean mExportDit, mExportNoc, mExportWmc, mExportFull, mUseCsvFormat;

    public ExportMetricFileResults(File directory, boolean exportDit, boolean exportNoc, boolean exportWmc, boolean exportFull, boolean useCsvFormat) {
        mDirectory = directory;
        mExportDit = exportDit;
        mExportNoc = exportNoc;
        mExportWmc = exportWmc;
        mExportFull = exportFull;
        mUseCsvFormat = useCsvFormat;
    }

    public File getDirectory() {
        return mDirectory;
    }

    public boolean exportDit() {
        return mExportDit;
    }

    public boolean exportNoc() {
        return mExportNoc;
    }

    public boolean exportWmc() {
        return mExportWmc;
    }

    public boolean exportFull() {
        return mExportFull;
    }

    public boolean useCsvFormat() {
        return mUseCsvFormat;
    }

}
