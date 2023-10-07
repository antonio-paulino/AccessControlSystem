LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Decoder_tb is
end Decoder_tb;

architecture teste of Decoder_tb is
	component Decoder is
	PORT( 
			S : in STD_LOGIC_VECTOR(1 downto 0);
	      I : out STD_LOGIC_vector(3 downto 0)
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;


	signal S_tb: std_logic_vector(1 downto 0); 
	signal I_tb: std_logic_vector(3 downto 0); 

	Begin 
	UUT: Decoder
			port map ( 
					S => S_TB,
					I => I_TB
				);
		stimulus: process
		
		begin
			wait for MCLK_PERIOD;
			S_TB <= "00";
			wait for MCLK_PERIOD;
			S_TB <= "01";
			wait for MCLK_PERIOD;
			S_TB <= "10";
			wait for MCLK_PERIOD;
			S_TB <= "11";
			wait;
		end process;
			
end teste;