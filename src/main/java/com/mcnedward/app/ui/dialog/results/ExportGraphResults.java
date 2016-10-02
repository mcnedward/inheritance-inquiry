package com.mcnedward.app.ui.dialog.results;

import java.io.File;

/**
 * Created by Edward on 10/2/2016.
 */
public class ExportGraphResults {

    private File mDirectory;
    private boolean mExportAll, mUsePackages, mUseProjectName;

    public ExportGraphResults(File directory, boolean exportAll, boolean usePackages, boolean useProjectName) {
        mDirectory = directory;
        mExportAll = exportAll;
        mUsePackages = usePackages;
        mUseProjectName = useProjectName;
    }

    public File getDirectory() {
        return mDirectory;
    }

    public boolean exportAll() {
        return mExportAll;
    }

    public boolean usePackages() {
        return mUsePackages;
    }

    public boolean useProjectName() {
        return mUseProjectName;
    }

}
