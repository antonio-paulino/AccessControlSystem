LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Adder4_tb is
end Adder4_tb;

architecture teste of Adder4_tb is
	component Adder4 is
	PORT( 
			A 	: in std_logic_vector(3 downto 0);
			B 	: in std_logic_vector(3 downto 0);
			CI : in std_logic;
			S 	: out std_logic_vector(3 downto 0);
			CO : out std_logic
		);	
	end component;

	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;


	signal CI_tb, CO_tb: std_logic; 
	signal A_tb, B_tb, S_tb : std_logic_vector(3 downto 0);



	Begin 
	UUT: Adder4
			port map ( 
				A  => a_tb,
				B  => B_tb,
				CI => CI_tb,
				S  => S_tb,
				CO => CO_tb
			);

	stimulus: process
		begin
			A_tb 	<= "0000";
			B_tb 	<= "0000";
			CI_tb 	<= '1';
			wait for MCLK_PERIOD;
			A_tb 	<= "1000";
			B_tb 	<= "0111";
			CI_tb <= '1';
			wait for MCLK_PERIOD;
			A_tb 	<= "1101";
			B_tb 	<= "0111";
			CI_tb <= '0';
			wait for MCLK_PERIOD;
			A_tb 	<= "1001";
			B_tb 	<= "0010";
			CI_tb <= '0';
			wait for MCLK_PERIOD;
			wait;
			
		end process;
		
end teste;