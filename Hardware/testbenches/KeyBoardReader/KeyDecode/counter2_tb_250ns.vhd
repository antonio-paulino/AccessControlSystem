LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY counterUp2_tb is
end counterUp2_tb;

architecture teste of counterUp2_tb is
	component counterUp2 is
		PORT( 
			RST 	: in STD_LOGIC;
			CE 	: in std_logic;
			CLK	: IN STD_LOGIC;
			Q 		: out std_logic_vector(1 downto 0)
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal clk_tb, rst_tb, ce_tb : std_logic; 
	signal Q_tb : std_logic_vector(1 downto 0);



	Begin 
	UUT: counterUp2
			port map ( 
					RST 	=> RST_tb,
					clk 	=> clk_tb,
					CE 	=> ce_tb,
					Q 		=> q_tb
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
			
			--Begin
			
			wait for MCLK_PERIOD*2;
			CE_tb 	<= '0';
			RST_tb 	<= '0';
			wait for MCLK_PERIOD*4;
			CE_tb 	<= '1';
			wait for MCLK_PERIOD*4;
			CE_tb 	<= '0';
			wait;	
			
		end process;
			
end teste;
