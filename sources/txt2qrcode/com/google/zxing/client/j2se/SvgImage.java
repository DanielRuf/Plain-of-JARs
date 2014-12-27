/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.j2se;

import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

/**
 * @author
 */
public final class SvgImage {

     private final StringBuilder content;

     public String getContent() { 
       return content.toString();
     }
     public void setContent(String value) { 
       content.setLength(0); if (value != null) content.append(value);
     }

     public SvgImage() {
        content = new StringBuilder();
     }

     public SvgImage(String content) {
        this.content = new StringBuilder(content);
     }

     public String toString() {
        return content.toString();
     }

     void addHeader() {
        content.append("<?xml version=\"1.0\" standalone=\"no\"?>");
        content.append("<!-- Created with ZXing (http://code.google.com/p/zxing/) -->");
        content.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
     }

     void addEnd() {
        content.append("</svg>");
     }
  
     void addTag(int displaysizeX, int displaysizeY, int viewboxSizeX, int viewboxSizeY, SvgImageColor background, SvgImageColor fill) {
    
        if (displaysizeX <= 0 || displaysizeY <= 0)
           content.append(String.format("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.2\" baseProfile=\"tiny\" viewBox=\"0 0 %d %d\" viewport-fill=\"rgb(%s)\" viewport-fill-opacity=\"%f\" fill=\"rgb(%s)\" fill-opacity=\"%f\" %s>",
               viewboxSizeX,
               viewboxSizeY,
               getColorRgb(background),
               convertAlpha(background),
               getColorRgb(fill),
               convertAlpha(fill),
               getBackgroundStyle(background)
               ));
        else
           content.append(String.format("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.2\" baseProfile=\"tiny\" viewBox=\"0 0 %d %d\" viewport-fill=\"rgb(%s)\" viewport-fill-opacity=\"%f\" fill=\"rgb(%s)\" fill-opacity=\"%f\" %s width=\"%d\" height=\"%d\">",
               viewboxSizeX,
               viewboxSizeY,
               getColorRgb(background),
               convertAlpha(background),
               getColorRgb(fill),
               convertAlpha(fill),
               getBackgroundStyle(background),
               displaysizeX,
               displaysizeY));
     }

     void addRec(int posX, int posY, int width, int height) {
        content.append(String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\"/>", posX, posY, width, height));
     }
  
     static double convertAlpha(SvgImageColor alpha) {
        return Math.round((((double)alpha.A) / (double)255));
     }
  
     static String getBackgroundStyle(SvgImageColor color) {
        double alpha = convertAlpha(color);
        return String.format("style=\"background-color:rgb(%d,%d,%d);background-color:rgba(%f);\"",
            color.R, color.G, color.B, alpha);
     }
  
     static String getColorRgb(SvgImageColor color) {
        return color.R + "," + color.G + "," + color.B;
     }

     static String getColorRgba(SvgImageColor color) {
        double alpha = convertAlpha(color);
        return color.R + "," + color.G + "," + color.B + "," + alpha;
     }
}