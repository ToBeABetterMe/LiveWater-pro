package com.livewater.comment.ui.materialList.listeners;

import android.support.annotation.NonNull;

import com.livewater.comment.ui.materialList.card.Card;

/**
 * The OnDismissCallback will be notified if a Card is dismissed.
 */
public interface OnDismissCallback {
    /**
     * A Card is dismissed.
     *
     * @param card
     *         which is dismissed.
     * @param position
     *         where the Card is.
     */
    void onDismiss(@NonNull final Card card, int position);
}
