// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.myapplication.utils;

//            LoopView, LoopListener

final class LoopRunnable implements Runnable {

    final com.myapplication.utils.LoopView loopView;

    LoopRunnable(com.myapplication.utils.LoopView loopview) {
        super();
        loopView = loopview;

    }

    public final void run() {
        com.myapplication.utils.LoopListener listener = loopView.loopListener;
        int i = com.myapplication.utils.LoopView.getSelectItem(loopView);
        listener.onItemSelect(i);
    }
}
