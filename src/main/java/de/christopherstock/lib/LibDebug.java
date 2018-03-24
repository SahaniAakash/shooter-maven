
    package de.christopherstock.lib;

    /*******************************************************************************************************************
    *   The interface for all debug systems.
    *******************************************************************************************************************/
    public interface LibDebug
    {
        /***************************************************************************************************************
        *   An output being logged if this debug group is enabled.
        ***************************************************************************************************************/
        void out(Object msg);

        /***************************************************************************************************************
        *   An output being logged UNCONDITIONAL.
        ***************************************************************************************************************/
        void err(Object msg);

        /***************************************************************************************************************
        *   A stacktrace being logged if this debug group is enabled.
        ***************************************************************************************************************/
        void trace(Throwable msg);

        /***************************************************************************************************************
        *   Displays memory info.
        ***************************************************************************************************************/
        void mem();
    }
