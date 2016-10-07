package com.mcnedward.app.utils;

import com.mcnedward.app.ui.dialog.*;
import com.mcnedward.app.ui.dialog.results.ExportAllGraphsResults;
import com.mcnedward.app.ui.dialog.results.ExportGraphResults;
import com.mcnedward.app.ui.dialog.results.ExportMetricFileResults;
import com.mcnedward.ii.listener.GitDownloadListener;
import com.mcnedward.ii.service.metric.MetricType;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by Edward on 10/1/2016.
 */
public class DialogUtils {

    private static ProjectFileDialog mFileDialog;
    private static GitDialog mGitDialog;
    private static ExportMetricFileDialog mExportMetricFileDialog;
    private static ExportAllGraphsDialog mExportGraphMetricDialog;
    private static ExportGraphDialog mExportGraphDialog;
    private static PreferencesDialog mPreferencesDialog;
    private static InfoDialog mDitInfoDialog;
    private static InfoDialog mNocInfoDialog;
    private static InfoDialog mWmcInfoDialog;
    private static InfoDialog mGitHelpDialog;
    private static AboutDialog mAboutDialog;
    private static MessageDialog mMessageDialog;

    public static void loadDialogs(JFrame parent, GitDownloadListener gitDownloadListener) {
        if (mFileDialog == null)
            mFileDialog = new ProjectFileDialog(parent);
        if (mGitDialog == null)
            mGitDialog = new GitDialog(parent, gitDownloadListener);
        if (mExportMetricFileDialog == null)
            mExportMetricFileDialog = new ExportMetricFileDialog(parent);
        if (mExportGraphMetricDialog == null)
            mExportGraphMetricDialog = new ExportAllGraphsDialog(parent);
        if (mExportGraphDialog == null)
            mExportGraphDialog = new ExportGraphDialog(parent);
        if (mPreferencesDialog == null)
            mPreferencesDialog = new PreferencesDialog(parent);
        if (mDitInfoDialog == null)
            mDitInfoDialog = new InfoDialog(parent, Constants.DIT_TITLE, Constants.DIT_MESSAGE);
        if (mNocInfoDialog == null)
            mNocInfoDialog = new InfoDialog(parent, Constants.NOC_TITLE, Constants.NOC_MESSAGE);
        if (mWmcInfoDialog == null)
            mWmcInfoDialog = new InfoDialog(parent, Constants.WMC_TITLE, Constants.WMC_MESSAGE);
        if (mGitHelpDialog == null)
            mGitHelpDialog = new InfoDialog(parent, Constants.GIT_HELP_TITLE, Constants.gitHelpMessage());
        if (mAboutDialog == null)
            mAboutDialog = new AboutDialog(parent);
        if (mMessageDialog == null)
            mMessageDialog = new MessageDialog(parent);
    }

    static Component[] getAppDialogs() {
        return new Component[] {
                mFileDialog, mGitDialog, mExportMetricFileDialog, mExportGraphMetricDialog, mExportGraphDialog,
                mPreferencesDialog, mDitInfoDialog, mNocInfoDialog, mWmcInfoDialog, mGitHelpDialog,
                mAboutDialog, mMessageDialog
        };
    }

    public static boolean openProjectFileDialog() {
        mFileDialog.open();
        return mFileDialog.isSuccessful();
    }

    public static File getProjectFile() {
        if (mFileDialog.isSuccessful())
            return mFileDialog.getDirectory();
        return null;
    }

    public static void openGitDialog() {
        mGitDialog.open();
    }

    public static boolean openExportMetricFileDialogForSuccess() {
        mExportMetricFileDialog.open();
        return mExportMetricFileDialog.isSuccessful();
    }

    public static ExportMetricFileResults getExportMetricGraphResults() {
        if (mExportMetricFileDialog.isSuccessful())
            return mExportMetricFileDialog.getResults();
        return null;
    }

    public static boolean openExportAllGraphsDialog() {
        mExportGraphMetricDialog.open();
        return mExportGraphMetricDialog.isSuccessful();
    }

    public static ExportAllGraphsResults getExportAllGraphResults() {
        if (mExportGraphMetricDialog.isSuccessful())
            return mExportGraphMetricDialog.getResults();
        return null;
    }

    public static boolean openExportGraphDialogForSuccess() {
        mExportGraphDialog.open();
        return mExportGraphDialog.isSuccessful();
    }

    public static ExportGraphResults getExportGraphDialogResults() {
        if (mExportGraphDialog.isSuccessful())
            return mExportGraphDialog.getResults();
        return null;
    }

    public static void openPreferencesDialog() {
        mPreferencesDialog.open();
    }

    public static void openMetricInfoDialog(MetricType metricType) {
        switch (metricType) {
            case DIT:
                openDitInfoDialog();
                break;
            case NOC:
                openNocInfoDialog();
                break;
            case WMC:
                openWmcInfoDialog();
                break;
        }
    }

    public static void openGitHelpDialog() {
        mGitHelpDialog.open();
    }

    public static void openMessageDialog(String message, String title) {
        mMessageDialog.setInfo(message, title);
        mMessageDialog.open();
    }

    public static void openAboutDialog() {
        mAboutDialog.open();
    }

    private static void openDitInfoDialog() {
        mDitInfoDialog.open();
    }

    private static void openNocInfoDialog() {
        mNocInfoDialog.open();
    }

    private static void openWmcInfoDialog() {
        mWmcInfoDialog.open();
    }

}
