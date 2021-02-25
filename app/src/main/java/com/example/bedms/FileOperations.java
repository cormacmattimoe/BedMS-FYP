/*

package com.example.bedms;

import android.graphics.Bitmap;
import android.media.Image;


import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class FileOperations() {


public Boolean write(String fname, Bitmap bm) {
    try {
        String fpath = "/sdcard/" + fname + ".pdf";
        File file = new File(fpath);
        if (!file.exists()) {
            file.createNewFile();
        }
        Document doc = new Document() {

        doc.open();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image myImg = Image.getInstance(stream.toByteArray());
        myImg.setAlignment(Image.MIDDLE);
        document.add(myImg);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

 */