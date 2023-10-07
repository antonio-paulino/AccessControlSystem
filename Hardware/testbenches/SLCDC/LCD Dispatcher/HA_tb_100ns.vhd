LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY HA_tb is
end HA_tb;

architecture teste of HA_tb is
	component HA is
	PORT( 
			A 	: in std_logic;
			B 	: in std_logic;
			S 	: out std_logic;
			CO : out std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal A_tb, B_tb, S_tb, CO_tb : std_logic; 



	Begin 
		UUT: HA
			port map ( 
				A	=> A_tb,
				B  => B_tb,
				S 	=> S_tb,
				CO => CO_tb
			);

		stimulus: process
		begin
			A_tb <= '0';
			B_tb <= '0';
			wait for MCLK_PERIOD;
			A_tb <= '1';
			B_tb <= '0';
			wait for MCLK_PERIOD;
			A_tb <= '1';
			B_tb <= '1';
		wait;
		
		end process;
		
end teste;