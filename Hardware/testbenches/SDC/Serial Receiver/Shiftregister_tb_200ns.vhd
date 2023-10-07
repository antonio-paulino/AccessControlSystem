LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Shiftregister_tb is
end Shiftregister_tb;

architecture teste of Shiftregister_tb is
	component Shiftregister is
	PORT( 
			I 		: in std_logic;
			EN 		: in STD_LOGIC;
			RST 	: in std_logic;
			CLK 	: IN STD_LOGIC;
			DATA 	: out std_logic_vector(4 downto 0)

		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;


	signal I_tb		: std_logic;
	signal EN_tb	: std_logic; 
	signal RST_tb	: std_logic; 
	signal CLK_tb 	: std_logic;
	signal DATA_tb	: std_logic_vector(4 downto 0);


	Begin 
	UUT: Shiftregister
			port map ( 
				I 		=> I_tb,
				EN 		=> EN_tb,
				RST 	=> RST_tb,
				CLK 	=> CLK_tb,
				DATA 	=> DATA_tb
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
			RST_tb 	<= '1';
			EN_tb 	<= '0';
			I_tb 		<= '0';
			--Begin
			wait for MCLK_PERIOD*2;
			EN_tb 	<= '1';
			RST_tb 	<= '0';
			wait for MCLK_PERIOD;
			I_tb <= '1';
			wait for MCLK_PERIOD;
			I_tb <= '0';
			wait for MCLK_PERIOD;
			I_tb <= '1';
			wait for MCLK_PERIOD;
			I_tb <= '0';
			wait for MCLK_PERIOD;
			I_tb <= '1';
			wait for MCLK_PERIOD;
			EN_tb <= '0'; 
			wait for MCLK_PERIOD;
			I_tb <= '1';
			wait for MCLK_PERIOD;
			I_tb <= '0';
			wait for MCLK_PERIOD;
			I_tb <= '1';
			wait for MCLK_PERIOD;
			I_tb <= '0';
			wait for MCLK_PERIOD;
			wait;
		end process;
end teste;