LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Keydecode_tb is
end Keydecode_tb;

architecture teste of Keydecode_tb is
	component KeyDecode is
	PORT( 
			RST		: IN std_logic;
			DATA_IN 	: in std_logic_vector(3 downto 0);
			K 			: out std_logic_vector(3 downto 0);
			Kval		: out std_logic;
			Osc		: in std_logic;
			DecE		: out std_logic_vector(2 downto 0);
			Kack		: in std_logic;
			Skpress	: out std_logic;
			Skscan	: out std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;


	signal RST_tb		: std_logic; 
	signal DATA_IN_tb	: std_logic_vector (3 downto 0);
	signal K_tb			: std_logic_vector (3 downto 0);
	signal Kval_tb		: std_logic; 
	signal Osc_tb		: std_logic; 
	signal DecE_tb		: std_logic_vector (2 downto 0);
	signal Kack_tb		: std_logic; 
	signal Kscan_tb	: std_logic;
	signal Kpress_tb	: std_logic;


	Begin 
	UUT: KeyDecode 
		port map ( 
			RST 		=> RST_tb,
			DATA_IN 	=> DATA_IN_tb,
			K 			=> K_tb,
			DecE 		=> DecE_tb,
			Kval 		=> Kval_tb,
	      Kack 		=> Kack_tb,
			Osc		=> Osc_tb,
	      Skpress 	=> Kpress_tb,
	      Skscan 	=> kscan_tb
		);
		
	clk_gen : process
	Begin
		Osc_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		Osc_tb <= '1';
		wait for MCLK_HALF_PERIOD;
	end process;

	stimulus: process
	begin
	--reset
		RST_tb 		<= '1';
		DATA_IN_tb 	<= "1111";
		Kscan_tb 	<= '0';
		Kack_tb 	<= '0';
	--Begin
		  wait for MCLK_PERIOD*2;
		  RST_tb <= '0';
		  wait for MCLK_PERIOD*2;
		  DATA_IN_tb <= "0111";
        wait for MCLK_PERIOD*4;
        Kack_tb <= '1';
        wait for MCLK_PERIOD*2;
        DATA_IN_tb 	<= "1111";
        wait for MCLK_PERIOD*4;
        Kack_tb <= '0';
        wait for MCLK_PERIOD*8;
	     DATA_IN_tb <= "1011";
        wait for MCLK_PERIOD*4;
        Kack_tb <= '1';
        wait for MCLK_PERIOD*2;
        DATA_IN_tb <= "1111";
        wait for MCLK_PERIOD*4;
        Kack_tb <= '0';
        wait for MCLK_PERIOD*8;
		  DATA_IN_tb  <= "1110";
        wait for MCLK_PERIOD*4;
        Kack_tb <= '1';
        wait for MCLK_PERIOD*2;
        DATA_IN_tb 	<= "1101";
        wait for MCLK_PERIOD*8;
        Kack_tb <= '0';

		wait;
		end process;
end teste;