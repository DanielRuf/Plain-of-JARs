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

import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Writes a {@link BitMatrix} to {@link SvgImage},
 * file or stream.
 *
 * @author
 */
public final class MatrixToSvgImageWriter {

  private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

  private MatrixToSvgImageWriter() {}

  /**
   * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
   * as white, and "true" bits are rendered as black.
   */
  public static SvgImage toSvgImage(BitMatrix matrix) {
    return toSvgImage(matrix, DEFAULT_CONFIG);
  }

  /**
   * As {@link #toSvgImage(BitMatrix)}, but allows customization of the output.
   */
  public static SvgImage toSvgImage(BitMatrix matrix, MatrixToImageConfig config) {
    SvgImage result = new SvgImage();

    create(result, matrix, config);
    
    return result;
  }

  private static void create(SvgImage image, BitMatrix matrix, MatrixToImageConfig config) {
     int quietZone = 5;

     if (matrix == null)
        return;

     int width = matrix.getWidth();
     int height = matrix.getHeight();
     image.addHeader();
     image.addTag(0, 0, 2 * quietZone + width, 2 * quietZone + height,
         new SvgImageColor(config.getPixelOffColor()), new SvgImageColor(config.getPixelOnColor()));
     appendDarkCell(image, matrix, quietZone, quietZone);
     image.addEnd();
  }

  private static void appendDarkCell(SvgImage image, BitMatrix matrix, int offsetX, int offSetY) {
     if (matrix == null)
        return;

     int width = matrix.getWidth();
     int height = matrix.getHeight();
     BitMatrix processed = new BitMatrix(width, height);
     boolean currentIsBlack = false;
     int startPosX = 0;
     int startPosY = 0;
     for (int x = 0; x < width; x++)
     {
        int endPosX;
        for (int y = 0; y < height; y++)
        {
           if (processed.get(x, y))
              continue;
        
           processed.set(x, y);
        
           if (matrix.get(x, y))
           {
              if (!currentIsBlack)
              {
                 startPosX = x;
                 startPosY = y;
                 currentIsBlack = true;
              }
           }
           else
           {
              if (currentIsBlack)
              {
                endPosX = findMaximumRectangle(matrix, processed, startPosX, startPosY, y);
                image.addRec(startPosX + offsetX, startPosY + offSetY, endPosX - startPosX + 1, y - startPosY);
                currentIsBlack = false;
              }
           }
        }
        if (currentIsBlack)
        {
          endPosX = findMaximumRectangle(matrix, processed, startPosX, startPosY, height);
          image.addRec(startPosX + offsetX, startPosY + offSetY, endPosX - startPosX + 1, height - startPosY);
          currentIsBlack = false;
        }
     }
  }
  
  private static int findMaximumRectangle(BitMatrix matrix, BitMatrix processed, int startPosX, int startPosY, int endPosY) {
     int endPosX = startPosX + 1;

     for (int x = startPosX + 1; x < matrix.getWidth(); x++)
     {
        for (int y = startPosY; y < endPosY; y++)
        {
           if (!matrix.get(x, y))
           {
              return endPosX;
           }
        }
        endPosX = x;
        for (int y = startPosY; y < endPosY; y++)
        {
           processed.set(x, y);
        }
     }
     
     return endPosX;
  }
  
  /**
   * Writes a {@link BitMatrix} to a file.
   *
   * @see #toSvgImage(BitMatrix)
   */
  public static void writeToFile(BitMatrix matrix, File file) throws IOException {
    writeToFile(matrix, file, DEFAULT_CONFIG);
  }

  /**
   * As {@link #writeToFile(BitMatrix, File)}, but allows customization of the output.
   */
  public static void writeToFile(BitMatrix matrix, File file, MatrixToImageConfig config) 
      throws IOException {  
    SvgImage image = toSvgImage(matrix, config);
    FileWriter writer = new FileWriter(file);
    writer.write(image.toString());
    writer.close();
  }
  
  /**
   * Writes a {@link BitMatrix} to a stream.
   *
   * @see #toSvgImage(BitMatrix)
   */
  public static void writeToStream(BitMatrix matrix, OutputStream stream) throws IOException {
    writeToStream(matrix, stream, DEFAULT_CONFIG);
  }

  /**
   * As {@link #writeToStream(BitMatrix, OutputStream)}, but allows customization of the output.
   */
  public static void writeToStream(BitMatrix matrix, OutputStream stream, MatrixToImageConfig config) 
      throws IOException {  
    SvgImage image = toSvgImage(matrix, config);
    OutputStreamWriter writer = new OutputStreamWriter(stream);
    writer.write(image.toString());
    writer.close();
  }

}
