LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Keycontrol_tb is
end Keycontrol_tb;

	architecture teste of Keycontrol_tb is
	component KeyControl is
	PORT( 
			RST 	: in std_logic; 
			clk 	: in std_logic;
			Kpress: in std_logic;
			Kscan : out std_logic;
			Kack 	: in std_logic;
			Kval 	: out std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal Kscan_tb	: std_logic;
	signal Kpress_tb	: std_logic;
	signal Kack_tb		: std_logic;
	signal Kval_tb		: std_logic;
	signal clk_tb		: std_logic; 
	signal RST_tb		: std_logic; 

	Begin 
	UUT: KeyControl 
			port map ( 
					RST  		=> RST_tb,
					clk  		=> clk_tb,
					Kpress 	=> Kpress_tb,
					Kscan  	=> Kscan_tb,
					Kack  	=> Kack_tb, 
					Kval  	=> Kval_tb
			);
			
	clk_gen : process
		Begin
			clk_tb <= '0';
			wait for MCLK_HALF_PERIOD;
			clk_tb <= '1';
			wait for MCLK_HALF_PERIOD;
		end process;
		

		stimulus: process
		begin
			--reset
			RST_tb <= '1';
			Kack_tb 	<= '0';
			Kpress_tb <= '0';
			--Begin
			wait for MCLK_PERIOD*2;
			RST_tb <= '0';
			wait for MCLK_PERIOD*2;
			Kack_tb <= '1';
			wait for MCLK_PERIOD*2;
			Kpress_tb <= '1';
			Kack_tb 	<= '0';
			wait for MCLK_PERIOD*2;
			Kack_tb 	<= '1';
			Kpress_tb <= '1';
			wait for MCLK_PERIOD*2;
			Kack_tb 	<= '0';
			wait for MCLK_PERIOD*2;
			Kpress_tb <= '0';

			wait;
		end process;
		
end teste;