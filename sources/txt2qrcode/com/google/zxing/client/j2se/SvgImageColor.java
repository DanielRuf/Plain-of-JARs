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

/**
 * @author
 */
public final class SvgImageColor {
     public static SvgImageColor Black = new SvgImageColor(0);
     public static SvgImageColor White = new SvgImageColor(0x00FFFFFF);

     public int A;
     public int R;
     public int G;
     public int B;

     public SvgImageColor(int color)
     {
        A = ((color & 0xFF000000) >> 24);
        R = ((color & 0x00FF0000) >> 16);
        G = ((color & 0x0000FF00) >> 8);
        B = ((color & 0x000000FF));
     }
}