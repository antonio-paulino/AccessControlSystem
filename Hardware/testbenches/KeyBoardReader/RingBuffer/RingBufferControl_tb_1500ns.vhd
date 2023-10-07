LIBRARY IEEE;
USE IEEE.std_logic_1164.all;

ENTITY RingBufferControl_TB IS
END RingBufferControl_TB;

ARCHITECTURE teste OF RingBufferControl_TB IS

    COMPONENT RingBufferControl IS
        PORT (
            clk     : in std_logic;
            RST     : in std_logic;
            DAV     : in std_logic;
            CTS     : in std_logic;
            full    : in std_logic;
            empty   : in std_logic;
            Wr      : out std_logic;
            selPG   : out std_logic;
            Wreg    : out std_logic;
            DAC     : out std_logic;
            incput  : out std_logic;
            incget  : out std_logic
        );
    END COMPONENT;
	 
	 constant MCLK_PERIOD: time:= 20 ns;
	 constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

  
	 signal clk_tb       : std_logic;
    signal rst_tb       : std_logic;
    signal dav_tb       : std_logic;
    signal cts_tb       : std_logic;
    signal full_tb      : std_logic;
    signal empty_tb     : std_logic;
    signal wr_tb        : std_logic;
    signal selPG_tb     : std_logic;
    signal wreg_tb      : std_logic;
    signal dac_tb       : std_logic;
    signal incput_tb    : std_logic;
    signal incget_tb    : std_logic;

	 Begin	 
    UUT : RingBufferControl
        PORT MAP (
            clk     => clk_tb,
            RST     => rst_tb,
            DAV     => dav_tb,
            CTS     => cts_tb,
            full    => full_tb,
            empty   => empty_tb,
            Wr      => wr_tb,
            selPG   => selPG_tb,
            Wreg    => wreg_tb,
            DAC     => dac_tb,
            incput  => incput_tb,
            incget  => incget_tb
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
        full_tb     <= '0';
        empty_tb    <= '0';
        WAIT FOR MCLK_PERIOD;
        rst_tb      <= '0';
	
	
	-- Testes DAV = '1'
	
        -- Teste 1 - not full -- escrita
		  
        dav_tb      <= '1';
        full_tb     <= '0';
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*6;
        dav_tb      <= '0'; 
        WAIT FOR MCLK_PERIOD*2;

        -- Teste 2 -  full, Clear to send -- envia
		
        dav_tb      <= '1';
        full_tb     <= '1';
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
        -- Teste 3 - full, not clear to send -- Não envia nem escreve
		  
        dav_tb      <= '1';
        full_tb     <= '1';
        cts_tb      <= '0';
        WAIT FOR MCLK_PERIOD*3;

  -- Testes DAV = '0'  
  
        -- Teste 1 - empty, clear to send -- Não envia
		  
        full_tb     <= '0';
        dav_tb      <= '0';
        empty_tb    <= '1';
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
		  
		  
		  -- Teste 2 - not empty, not clear to send -- Não envia
		  
        dav_tb      <= '0';
        empty_tb    <= '0';
        cts_tb      <= '0';
        WAIT FOR MCLK_PERIOD*3;
		  
		  
		  -- Teste 3 - not empty, clear to send -- envia
		  
		  dav_tb      <= '0';
        empty_tb    <= '0';
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*6;
		  cts_tb      <= '0';
        WAIT FOR MCLK_PERIOD*6;
		  
 -- Teste - full to not full with clear to send - dav = 1
 
		  dav_tb      <= '1';
        full_tb     <= '1';
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
        full_tb     <= '0';
		  cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
        dav_tb      <= '0'; 
        WAIT FOR MCLK_PERIOD*2;
		
 -- Teste - empty to not empty with clear to send - dav = 0
 
        dav_tb      <= '0';
        empty_tb    <= '1';
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        empty_tb    <= '0';
        cts_tb      <= '1';
        WAIT FOR MCLK_PERIOD*3;
        cts_tb      <= '0';
		  WAIT FOR MCLK_PERIOD*3;
		  
        WAIT;
    END PROCESS;

END teste;