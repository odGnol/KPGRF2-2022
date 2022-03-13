package rasterize;

import java.util.Optional;

public interface Raster<E> {

    default boolean isInsideBounds(int x, int y) {
//        getWidth()
//        getHeight()
        return true;
    }

    /**
     * Clear raster
     */
    void clear();

    /**
     * Set clear value
     *
     * @param clearValue clear value
     */
    void setClearValue(E clearValue);

    /**
     * Get horizontal size
     *
     * @return width
     */
    int getWidth();

    /**
     * Get vertical size
     *
     * @return height
     */
    int getHeight();

    /**
     * Get element value at [x,y] position
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return element value
     */
    Optional<E> getElement(int x, int y);

    /**
     * Set element value at [x,y] position
     *
     * @param x     horizontal coordinate
     * @param y     vertical coordinate
     * @param value element value
     */
    void setElement(int x, int y, E value);
}
