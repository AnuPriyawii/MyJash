package com.myjash.app.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.myjash.app.AppUtil.HeaderAction;
import com.myjash.app.AppUtil.Util;
import com.myjash.app.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.Inflater;

public class PdfViewer extends Fragment {
    TextView tv_loading;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    //    hotelpodlipou.sk/uploads/files/sample.pdf
    String download_file_url = "";
    //    String download_file_url = "http://www.princexml.com/samples/catalog/PrinceCatalogue.pdf";
    float per = 0;
    PDFView pdfView;
    View view;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
        tv_loading = (TextView) view.findViewById(R.id.txtMsg);
        pdfView = (PDFView) view.findViewById(R.id.pdfview);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);


        /*Set header*/
        new HeaderAction(view, getActivity());

        progressBar.getProgressDrawable().setColorFilter(
                Color.parseColor("#41c750"), android.graphics.PorterDuff.Mode.SRC_IN);

        download_file_url = getArguments().getString("url");
        Log.d("download_file_url", download_file_url + "d");
        if (Util.haveNetworkConnection(getActivity()))
            downloadAndOpenPDF();
        else
            tv_loading.setText("No internet!!!");
        return view;
    }

    void downloadAndOpenPDF() {
        new AsyncTask<String, String, File>() {
            @Override
            protected File doInBackground(String... params) {
                File file = null;
                try {
                    try {
                        file = downloadFile(download_file_url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return file;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                try {
                    if (file != null) {
                        Log.d("FileDownloaded", file.toString());
                        if (file != null) {
                            pdfView.fromFile(file)
//                                .pages(0, 2, 1, 3, 3, 3)
                                    .defaultPage(1)
                                    .showMinimap(false)
                                    .enableSwipe(true)
                                    .swipeVertical(true)
                                    .onDraw(new com.joanzapata.pdfview.listener.OnDrawListener() {
                                        @Override
                                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                                        }
                                    })
                                    .onLoad(new OnLoadCompleteListener() {
                                        @Override
                                        public void loadComplete(int nbPages) {
                                            Log.d("PagesComplet", nbPages + "fgh");
                                            pdfView.setVisibility(View.VISIBLE);
                                        }
                                    })
                                    .onPageChange(new OnPageChangeListener() {
                                        @Override
                                        public void onPageChanged(int page, int pageCount) {

                                        }
                                    })
                                    .load();
                        } else {
                            tv_loading.setText("Unable to download...");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();
            int status = urlConnection.getResponseCode();
            Log.d("StatusGet", "d" + String.valueOf(status) + "s");
            if (!String.valueOf(status).startsWith("4") && !String.valueOf(status).startsWith("5")) {
                // set the path where we want to save the file
                File SDCardRoot = Environment.getExternalStorageDirectory();
                // create a new file, to save the downloaded file
                file = new File(SDCardRoot, dest_file_path);

                FileOutputStream fileOutput = new FileOutputStream(file);

                // Stream used for reading the data from the internet

                Log.d("StatusGet", "d" + String.valueOf(status) + "s");
                InputStream inputStream = urlConnection.getInputStream();

                // this is the total size of the file which we are
                // downloading
                totalsize = urlConnection.getContentLength();
                setText("Starting PDF download...");

                // create a buffer...
                byte[] buffer = new byte[1024 * 1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalsize) * 100;
                    int totalPdfFileSize = totalsize / 1024;
                    setText("Downloading PDF " + (int) per
                            + "% complete");
                    progressBar.setProgress((int) per);
                }
                // close the output stream when complete //
                fileOutput.close();
                setText("Download Complete");
            } else {
                setText("PDF file is corrupted");
            }

        } catch (final MalformedURLException e) {
            setTextError("Malformed url",
                    Color.RED);
        } catch (final IOException e) {
            Log.d("IOException", e.toString());
            setTextError("IOExcption",
                    Color.RED);
        } catch (final Exception e) {
            setTextError(
                    "Failed to download pdf. Please check your internet connection.",
                    Color.RED);
        }
        return file;
    }

    void setTextError(final String message, final int color) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    tv_loading.setTextColor(color);
                    tv_loading.setText(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void setText(final String txt) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }
}
