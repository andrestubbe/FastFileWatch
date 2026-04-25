package fastfilewatch;

import fastfilesearch.FileUpdate;

/**
 * WatchCallback - Callback for file watch events.
 */
public interface WatchCallback {
    void onUpdate(FileUpdate update);
}
