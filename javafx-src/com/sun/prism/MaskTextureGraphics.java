/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.prism;

/*
 * For now, this interface works on RTTextures simply because the only use
 * case that currently uses these methods has RTTextures to provide and
 * because the J2D pipeline has only implemented these methods for the
 * underlying pixel format that it uses for RTTextures and so handing it an
 * arbitrary texture that may potentially use another format may not succeed.
 */
public interface MaskTextureGraphics extends Graphics {
    /**
     * Apply a mask to the pixels of an image and render the results onto
     * the destination in device space.
     * All coordinates specify pixel coordinates and are integers to reinforce
     * the fact that these are pixel operations, not coordinate rendering
     * operations.
     * <p>
     * The image texture is combined with the mask texture by multiplying the
     * pixels in the image texture by the alpha in the mask texture and then
     * the result is applied to the destination using the current compositing
     * rule (which should likely be SrcOver in most cases).
     * <pre>
     *     dst.argb = (img.argb * mask.a) Composite dst.argb
     * </pre>
     * 
     * @param imgtex  the texture containing the source image pixels
     * @param masktex the texture containing the mask pixels, only the
     *                alpha channel is used from this texture
     * @param dx the X coordinate of the UL destination pixel
     * @param dy the Y coordinate of the UL destination pixel
     * @param dw the width of the pixel regions to be combined
     * @param dh the height of the pixel regions to be combined
     * @param ix the X coordinate of the UL pixel in the image texture
     * @param iy the Y coordinate of the UL pixel in the image texture
     * @param mx the X coordinate of the UL pixel in the mask texture
     * @param my the Y coordinate of the UL pixel in the mask texture
     */
    public void drawPixelsMasked(RTTexture imgtex, RTTexture masktex,
                                 int dx, int dy, int dw, int dh,
                                 int ix, int iy, int mx, int my);

    /**
     * Use a mask to determine which pixels of an image are to be rendered
     * onto the destination in device space.
     * All coordinates specify pixel coordinates and are integers to reinforce
     * the fact that these are pixel operations, not coordinate rendering
     * operations.
     * <p>
     * The mask texture controls the contribution of source and destination
     * pixels in the resulting output.
     * Note that a simple multiply of the source texture by the mask texture
     * may not produce the correct masking operation for the case where a
     * non-SrcOver blending mode is in use.
     * In those conditions, it is more accurate to first compute the result
     * of blending the source into the destination and to then use this
     * method to choose how much of the result should be taken from the
     * results of the blending and how much should remain the original
     * destination pixel value.
     * Mathematically, the mask alpha controls a linear interpolation
     * between the image and destination pixels.
     * <pre>
     *     dst.argb = (mask.a * img.argb) + ((1 - mask.a) * dst.argb)
     * </pre>
     * Note that the current composite mode is ignored during this operation.
     * 
     * @param imgtex  the texture containing the source image pixels
     * @param masktex the texture containing the mask pixels, only the
     *                alpha channel is used from this texture
     * @param dx the X coordinate of the UL destination pixel
     * @param dy the Y coordinate of the UL destination pixel
     * @param dw the width of the pixel regions to be combined
     * @param dh the height of the pixel regions to be combined
     * @param ix the X coordinate of the UL pixel in the image texture
     * @param iy the Y coordinate of the UL pixel in the image texture
     * @param mx the X coordinate of the UL pixel in the mask texture
     * @param my the Y coordinate of the UL pixel in the mask texture
     */
    public void maskInterpolatePixels(RTTexture imgtex, RTTexture masktex,
                                      int dx, int dy, int dw, int dh,
                                      int ix, int iy, int mx, int my);
}
