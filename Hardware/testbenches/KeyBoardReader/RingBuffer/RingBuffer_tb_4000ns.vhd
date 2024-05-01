LIBRARY IEEE;
USE IEEE.std_logic_1164.all;

ENTITY RingBuffer_TB IS
END RingBuffer_TB;

ARCHITECTURE teste OF RingBuffer_TB IS

    COMPONENT RingBuffer IS
        PORT (
            D       : in std_logic_vector(3 downto 0);
            clk     : in std_logic;
				RST     : in std_logic;
				DAV     : in std_logic;
				CTS     : in std_logic;
            DAC     : out std_logic;
            Wreg    : out std_logic;
				Q       : out std_logic_vector(3 downto 0)
        );
    END COMPONENT;
	 
	 constant MCLK_PERIOD: time:= 20 ns;
	 constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

  
    signal D_tb         : std_logic_vector(3 downto 0);
    signal CLK_tb       : std_logic;
    signal DAV_tb       : std_logic;
    signal CTS_tb       : std_logic;
    signal DAC_tb       : std_logic;
    signal WREG_tb      : std_logic;
	 signal RST_tb       : std_logic;
    signal Q_tb         : std_logic_vector(3 downto 0);


	 Begin	 
    UUT : RingBuffer
        PORT MAP (
				rst 	  => RST_tb,
            D       => D_tb,
            CLK     => CLK_tb,
            DAV     => dav_tb,
            CTS     => cts_tb,
            DAC     => DAC_tb,
            WREG    => WREG_tb,
            Q       => Q_tb
        );

    clk_gen : process
	 Begin
			clk_tb <= '0';
			wait for MCLK_HALF_PERIOD;
			clk_tb <= '1';
			wait for MCLK_HALF_PERIOD;
		end process;
		
	stimulus: process
   Begin
        -- Reset 
        rst_tb      <= '1';
        dav_tb      <= '0';
        cts_tb      <= '0';
        D_tb        <= "0000";
        WAIT FOR MCLK_PERIOD;
        rst_tb      <= '0';
	
	
    --Escrita até full + 1 para verificar overflow
        DAV_tb <= '1';
        D_tb 	<= "0001";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "0010";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "0101";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "1010";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "1110";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "1011";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "0000";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "0011";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        D_tb <= "0110";
        WAIT FOR MCLK_PERIOD*4;
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        DAV_tb <= '1';
        WAIT FOR MCLK_PERIOD;


    -- Full com cts = 0 e DAV = 1
        cts_tb <= '0';
        WAIT FOR MCLK_PERIOD*5;

    --Leitura até empty + 1 para verificar overflow
        DAV_tb <= '0';
       cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;

    -- Escritas e leituras sucessivas
        cts_TB <= '1';
        DAV_tb <= '1';
        D_tb <= "1110";
        WAIT FOR MCLK_PERIOD*4; -- Escrita
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD*5; -- Leitura
        CTS_tb <= '0';
		  WAIT FOR MCLK_PERIOD*3;
        DAV_tb <= '1';
        D_tb <= "1010";
        WAIT FOR MCLK_PERIOD*4; -- Escrita
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD*5; -- Esperar com CTS = 0 após uma escrita
        DAV_tb <= '1';
        D_tb <= "0011";
        WAIT FOR MCLK_PERIOD*4; -- Escrita
        DAV_tb <= '0';
        WAIT FOR MCLK_PERIOD*2; 
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3; -- Leituras

    -- Empty com cts = 1 
        WAIT FOR MCLK_PERIOD*5;



        WAIT;
    END PROCESS;

END teste;