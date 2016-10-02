package com.mcnedward.app.ui.dialog.results;

import java.io.File;

/**
 * Created by Edward on 10/2/2016.
 */
public class ExportAllGraphsResults {

    private File mDirectory;
    private boolean mExportDit, mExportNoc, mExportWmc, mExportFull, mUsePackages, mUseProjectName;

    public ExportAllGraphsResults(File directory, boolean exportDit, boolean exportNoc, boolean exportWmc, boolean exportFull, boolean usePackages, boolean useProjectName) {
        mDirectory = directory;
        mExportDit = exportDit;
        mExportNoc = exportNoc;
        mExportWmc = exportWmc;
        mExportFull = exportFull;
        mUsePackages = usePackages;
        mUseProjectName = useProjectName;
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

    public boolean usePackages() {
        return mUsePackages;
    }

    public boolean useProjectName() {
        return mUseProjectName;
    }

}
