LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY KeyScan_tb is
end KeyScan_tb;

	architecture teste of KeyScan_tb is
	component KeyScan is
		PORT( 
				RST 		: in std_logic;
				DATA_IN	: in std_logic_vector(3 downto 0);
				Kscan 	: in STD_LOGIC;
				K 			: out std_logic_vector(3 downto 0);
				Kpress	: out std_logic;
				Osc		: in std_logic;
				DecE		: out std_logic_vector(2 downto 0)
			);	
	end component;
	
	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;
	
	signal DATA_IN_tb	: std_logic_vector (3 downto 0);
	signal DecE_tb		: std_logic_vector (2 downto 0);
	signal Kscan_tb	: std_logic;
	signal K_tb			: std_logic_vector (3 downto 0);
	signal Kpress_tb	: std_logic;
	signal Osc_tb		: std_logic; 
	signal RST_tb		: std_logic; 
	
	Begin 
	UUT: KeyScan 
			port map ( 
				DecE 		=> DecE_tb,
				RST 		=> RST_tb,
				DATA_IN 	=> DATA_IN_tb,
				Kscan 	=> Kscan_tb,
				K 			=> K_tb,
				Kpress 	=> Kpress_tb,
				Osc 		=> Osc_tb
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
			--Begin
			wait for MCLK_PERIOD*2;
			RST_tb 		<= '0';
			Kscan_tb 	<= '1';
			wait for MCLK_PERIOD*15;
			DATA_IN_tb 	<= "0111";
			wait for MCLK_PERIOD*2;
			Kscan_tb 	<= '0';
			wait for MCLK_PERIOD;
			DATA_IN_tb 	<= "1111";
			wait for MCLK_PERIOD*5;
			Kscan_tb 	<= '1';
			DATA_IN_tb 	<= "1101";
			wait for MCLK_PERIOD*2;
			Kscan_tb 	<= '0';
			wait for MCLK_PERIOD;
			DATA_IN_tb 	<= "1111";
			wait for MCLK_PERIOD*5;
			Kscan_tb 	<= '1';
			DATA_IN_tb 	<= "1110";
			wait for MCLK_PERIOD*2;
			Kscan_tb 	<= '0';
			wait for MCLK_PERIOD;
			DATA_IN_tb 	<= "1111";
			wait for MCLK_PERIOD*5;
			Kscan_tb 	<= '1';
			DATA_IN_tb 	<= "1011";
			wait for MCLK_PERIOD*2;
			Kscan_tb 	<= '0';
			wait for MCLK_PERIOD;
			DATA_IN_tb 	<= "1111";
			wait;
		end process;
		
end teste;