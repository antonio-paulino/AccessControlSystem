LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Penc_tb is
end Penc_tb;

architecture teste of Penc_tb is
	component Penc4x2 is
	   port(	
			I	: in std_logic_vector(3 downto 0);
			O 	: out STD_LOGIC_VECTOR(1 DOWNTO 0);
			GS	: out STD_LOGIC
	   );
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

	signal I_tb		: std_logic_vector (3 downto 0);
	signal O_tb		: std_logic_vector (1 downto 0);
	signal GS_tb	: std_logic;

	Begin 
		UUT: Penc4x2 
			port map ( 
				I 	=> I_tb,
				O 	=> O_tb,
				GS 	=> GS_tb
			);

		stimulus: process
		begin
			--Begin
			I_tb <= "0000";
			wait for MCLK_PERIOD;
			I_tb <= "0001";
			wait for MCLK_PERIOD;
			I_tb <= "0011";
			wait for MCLK_PERIOD;
			I_tb <= "0111";
			wait for MCLK_PERIOD;
			I_tb <= "1111";
			wait for MCLK_PERIOD;
			I_tb <= "1000";
			wait for MCLK_PERIOD;
			I_tb <= "0100";
			wait for MCLK_PERIOD;
			I_tb <= "0010";
			wait for MCLK_PERIOD;
			I_tb <= "0001";
			wait;

			end process;
end teste;