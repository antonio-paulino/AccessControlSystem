LIBRARY IEEE;
USE IEEE.std_logic_1164.all;

ENTITY MAC_tb IS
END MAC_tb;

ARCHITECTURE teste OF MAC_tb IS

    COMPONENT MAC IS
        PORT (
            RST		: in std_logic;
				Addr 		: out std_logic_vector(2 downto 0);
				putget	: in std_logic;
				incget 	: in std_logic;
				incput 	: in std_logic;
				full 		: out std_logic;
				empty 	: out std_logic
        );
    END COMPONENT;
	 
	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal	RST_tb		:  std_logic;
	signal	Addr_tb     :  std_logic_vector(2 downto 0);
	signal	putget_tb	:  std_logic;
	signal	incget_tb 	:  std_logic;
	signal	incput_tb 	:  std_logic;
	signal	full_tb     :  std_logic;
	signal	empty_tb 	:  std_logic;


    Begin	 
    UUT : MAC
        PORT MAP (
            RST     => RST_TB,
            ADDR    => ADDR_TB,
            PUTGET  => PUTGET_TB,
            INCGET  => INCGET_TB,
            INCPUT  => INCPUT_TB,
            FULL    => FULL_TB,
            EMPTY   => EMPTY_TB
        );
		  
    stimulus: process
    Begin
       
        --RESET
        RST_TB    <= '1';
        INCPUT_TB <= '0';
        INCGET_TB <= '0';

        WAIT FOR MCLK_PERIOD;
        RST_TB    <= '0';

    -- escrita até full (1000 - 0000)
		  putget_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';

    -- leitura até empty (1000 - 1000)
		  putget_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
		  
	 -- escrita até full (0000 - 1000)
		  putget_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
		  
	-- leitura até empty (0000 - 0000)
		  putget_tb <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        
    --Escritas e leituras sucessivas
		  putget_tb <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
		  putget_tb <= '0';
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
		  putget_tb <= '1';
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
		  putget_tb <= '0';
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
		  putget_tb <= '1';
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
		  putget_tb <= '0';
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
        WAIT FOR MCLK_PERIOD;
		  putget_tb <= '1';
        INCPUT_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCPUT_TB <= '0';
        WAIT FOR MCLK_PERIOD;
		  putget_tb <= '0';
        INCGET_TB <= '1';
        WAIT FOR MCLK_PERIOD;
        INCGET_TB <= '0';
	
    WAIT;
    END PROCESS;

END teste;