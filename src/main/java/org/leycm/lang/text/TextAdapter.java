package org.leycm.lang.text;

import org.leycm.adapter.ObjectAdapter;

/**
 * Interface for converting between Text objects and other formats
 * @param <T> The target type for conversion
 */
public interface TextAdapter<T> extends ObjectAdapter<Text, T> {
}