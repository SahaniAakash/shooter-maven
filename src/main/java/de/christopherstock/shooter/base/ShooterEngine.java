
    package de.christopherstock.shooter.base;

    import  java.awt.*;
    import  java.awt.image.*;
    import  java.io.*;
    import  javax.imageio.*;
    import  de.christopherstock.lib.gl.*;
    import  de.christopherstock.lib.io.*;
    import  de.christopherstock.lib.ui.LibFPS;
    import  de.christopherstock.lib.ui.LibUI;
    import  de.christopherstock.shooter.*;
    import  de.christopherstock.shooter.ShooterSetting.*;
    import  de.christopherstock.shooter.game.objects.Player;
    import  de.christopherstock.shooter.io.hid.lwjgl.*;
    import  de.christopherstock.shooter.io.sound.*;
    import  de.christopherstock.shooter.level.*;
    import  de.christopherstock.shooter.state.*;
    import  de.christopherstock.shooter.ui.hud.*;
    import  org.lwjgl.opengl.Display;

    /*******************************************************************************************************************
    *   The game engine holding all engine systems.
    *******************************************************************************************************************/
    public class ShooterEngine
    {
        /** Icon image for the frame. TODO outsource! */
        private                     BufferedImage           iconImage                   = null;
        /** The bg image for the preloader. TODO outsource! */
        private                     BufferedImage           bgImage                     = null;

        /** Preloader. */
        public                      Preloader               preloader                   = null;
        /** Heads up display. */
        public                      HUD                     hud                         = null;
        /** Frames per second counter. */
        public                      LibFPS                  fps                         = null;
        /** Current main state. */
        public                      MainState               mainState                   = MainState.EPreloader;
        /** Main state to change to. */
        public                      MainState               mainStateToChangeTo         = null;

        /** The global player-instance being controlled by the user. */
        public                      Player                  player                      = null;

        /** The frame contains a native Java frame and canvas. */
        public                      LibFrame                frame                       = null;
        /** The gl view manages all GL operations. */
        public                      LibGLView               glView                      = null;

        /***************************************************************************************************************
        *   Inits the ui.
        ***************************************************************************************************************/
        protected void initUi()
        {
            ShooterDebug.init.out( "init UI" );

            try
            {
                this.iconImage = ImageIO.read( LibIO.preStreamJarResource( ShooterSetting.Path.EScreen.url + "icon.png" ) );
                this.bgImage   = ImageIO.read( LibIO.preStreamJarResource( ShooterSetting.Path.EScreen.url + "bg.jpg"   ) );
            }
            catch ( IOException ioe )
            {
                ShooterDebug.error.err( "ERROR! Screen-Graphics could not be loaded!" );
            }

            //set host-os lookAndFeel
            LibUI.setLookAndFeel( ShooterDebug.error );
        }

        /***************************************************************************************************************
        *   Inits the preloader.
        ***************************************************************************************************************/
        protected void initPreloader()
        {
            ShooterDebug.init.out( "init Preloader" );

            this.preloader = new Preloader
            (
                new LibGLImage( this.bgImage, LibGLImage.ImageUsage.EOrtho, ShooterDebug.glImage, true  )
            );
        }

        /***************************************************************************************************************
        *   Inits the Open GL system.
        ***************************************************************************************************************/
        protected void initGL()
        {
            ShooterDebug.init.out( "init GL" );

            if ( ShooterDebug.ENABLE_FULLSCREEN && !ShooterDebug.DEBUG_MODE )
            {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

                ShooterSetting.Form.FORM_WIDTH  = screenSize.width;
                ShooterSetting.Form.FORM_HEIGHT = screenSize.height;
            }


/*
            this.gl = new LibGL();
            this.gl.init
            (
                ShooterSetting.Form.FORM_WIDTH,
                ShooterSetting.Form.FORM_HEIGHT,
                ShooterSetting.Form.FORM_TITLE,
                this.iconImage,
                ShooterDebug.init
            );
*/
            //init frame
            this.frame = new LibFrame
            (
                ShooterSetting.Form.FORM_TITLE,
                ShooterSetting.Form.FORM_WIDTH,
                ShooterSetting.Form.FORM_HEIGHT,
                this.iconImage
            );

            //init lwjgl glView
            this.glView = new LibGLView
            (
                ShooterDebug.init,
                this.frame,
                ShooterSetting.Form.FORM_WIDTH,
                ShooterSetting.Form.FORM_HEIGHT
            );
            this.glView.init();
        }

        /***************************************************************************************************************
        *   Inits the rest.
        ***************************************************************************************************************/
        protected void initRest()
        {
            this.preloader.increase( 10 );
            LWJGLMouse.init();

            this.preloader.increase( 20 );
            this.initFonts();

            this.preloader.increase( 30 );
            ShooterTexture.loadImages();

            //assign textures and perform repaint
            this.preloader.increase( 40 );
            this.glView.initTextures( ShooterTexture.getAllTextureImages() );

            //init 3d studio max objects and perform repaint
            this.preloader.increase( 50 );
            ShooterD3ds.init( ShooterDebug.d3ds );

            //init hud
            this.preloader.increase( 60 );
            this.hud = new HUD();
            this.fps = new LibFPS( Fonts.EFps, ShooterSetting.Colors.EFpsFg.colABGR, ShooterSetting.Colors.EFpsOutline.colABGR, ShooterDebug.glImage );

            //init HUD fx
            HUDFx.init();

            //init sounds and bg sounds
            this.preloader.increase( 70 );
            SoundFg.init();

            this.preloader.increase( 80 );
            SoundBg.init();

            //switch main state to 'game' and order change to level 1
            this.preloader.increase( 90 );

            //init main menu
            MainMenu.init();

            this.preloader.increase( 100 );

            //reset and change to startup main state
            Shooter.game.orderMainStateChangeTo( ShooterSetting.Startup.STARTUP_STATE_AFTER_PRELOADER );
            LevelChange.orderLevelChange( Startup.STARTUP_LEVEL_MAIN, Startup.STARTUP_LEVEL_SECTION, true );
        }

        private void initFonts()
        {
            try
            {
                Fonts.EAmmo          = LibIO.createFont( Path.EFont.url + "sourceSansPro.otf", 18.0f ); // new Font( "verdana",     Font.BOLD,  12 );
                Fonts.EPreloader     = LibIO.createFont( Path.EFont.url + "sourceSansPro.otf", 25.0f ); // new Font( "verdana",     Font.BOLD,  12 );
                Fonts.EHealth        = LibIO.createFont( Path.EFont.url + "sourceSansPro.otf", 18.0f ); // new Font( "verdana",     Font.BOLD,  12 );
                Fonts.EFps           = LibIO.createFont( Path.EFont.url + "sourceSansPro.otf", 18.0f ); // new Font( "verdana",     Font.BOLD,  12 );
                Fonts.EAvatarMessage = LibIO.createFont( Path.EFont.url + "sourceSansPro.otf", 18.0f ); // new Font( "verdana",     Font.BOLD,  12 );

                Fonts.EMainMenu      = LibIO.createFont( Path.EFont.url + "sourceSansPro.otf", 55.0f );
            }
            catch ( Throwable t )
            {
                ShooterDebug.error.trace( t );
                System.exit( 1 );
            }
        }

        public void destroy()
        {
            Display.destroy();
            System.exit( 0 );
        }
    }
