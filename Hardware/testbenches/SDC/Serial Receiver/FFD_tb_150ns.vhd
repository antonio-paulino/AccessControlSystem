LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY FFD_tb is
end FFD_tb;

architecture teste of FFD_tb is
component FFD is
PORT( 
		CLK 	: in std_logic;
		RESET : in STD_LOGIC;
		SET 	: in std_logic;
		D 		: IN STD_LOGIC;
		EN		: IN STD_LOGIC;
		Q 		: out std_logic
	);	
end component;

constant MCLK_PERIOD: time:= 20 ns;
constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

signal CLK_tb, RESET_tb, SET_tb, D_tb, EN_tb, Q_tb: std_logic; 



Begin 
UUT: FFD
		port map ( 
			CLK 	=> CLK_tb,
			RESET => RESET_tb,
			SET 	=> SET_tb,
			D 	 	=> D_tb,
			EN		=> EN_tb,
			Q 		=> Q_tb
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
		D_tb 		<= '1';
		reset_tb <= '1';
		EN_tb 	<= '0';
		wait for MCLK_PERIOD;
		reset_tb <= '0';
		wait for MCLK_PERIOD;
		set_tb 	<= '1';
		wait for MCLK_PERIOD;
		set_tb 	<= '0';
		wait for MCLK_PERIOD;
		EN_tb 	<= '1';
		wait for MCLK_PERIOD;
		d_tb 		<= '0';
		wait;
	end process;
end teste;