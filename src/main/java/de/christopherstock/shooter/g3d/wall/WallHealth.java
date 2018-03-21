
    package de.christopherstock.shooter.g3d.wall;

    /*******************************************************************************************************************
    *   Represents a mesh with all faces collision enabled.
    *******************************************************************************************************************/
    public enum WallHealth
    {
        EUnbreakale(        0   ),
        EGlass(             15  ),
        EWoodBrittle(       10  ),
        EWoodNormal(        15  ),
        EElectricalDevice(  15  ),
        ESolidGlass(        20  ),
        ESolidWood(         25  ),
        EVendingMachine(    25  ),
        ECrate(             30  ),
        EFurniture(         50  ),
        ;

        private     int     iHealth     = 0;

        private WallHealth( int aHealth )
        {
            iHealth = aHealth;
        }

        public int getHealth()
        {
            return iHealth;
        }
    }
