LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY register3_tb is
end register3_tb;

architecture teste of register3_tb is
	component register3 is
	PORT( 
			I 		: in std_logic_vector(2 downto 0);
			EN 	: in STD_LOGIC;
			RST 	: in std_logic;
			CLK 	: IN STD_LOGIC;
			Q 		: out std_logic_vector(2 downto 0)
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal EN_tb, RST_tb, CLK_tb: std_logic; 
	signal I_tb, Q_tb : std_logic_vector(2 downto 0);


	Begin 
	UUT: register3 
			port map ( 
				I  	=> I_tb,
				EN  	=> EN_tb,
				RST 	=> RST_tb,
				CLK  	=> CLK_tb,
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
			rst_tb <= '1';
			EN_tb <= '0';
			wait for MCLK_PERIOD;
			rst_tb <= '0';
			wait for MCLK_PERIOD;
			I_tb 	<= "111";
			wait for MCLK_PERIOD;
			I_tb 	<= "101";
			wait for MCLK_PERIOD;
			EN_tb <= '1';
			wait for MCLK_PERIOD;
			I_tb 	<= "011";
			wait;
		end process;

end teste;