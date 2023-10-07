LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Adder3 is
	port (
		A 	: in std_logic_vector(2 downto 0);
		B 	: in std_logic_vector(2 downto 0);
		CI : in std_logic;
		S 	: out std_logic_vector(2 downto 0);
		CO : out std_logic
	);
end Adder3;

architecture logic of Adder3 is

	component FA is
		port (
			A 	: in std_logic;
			B 	: in std_logic;
			CI : in std_logic;
			S 	: out std_logic;
			CO : out std_logic
		);
	end component;
	
	signal sCO : std_logic_vector(2 downto 0);

	begin

	FA0 : FA port map (
				A 	=> A(0),
				B 	=>  B(0),
				CI => CI,
				S  => S(0),
				CO  => sCO(0)
	);
	
	FA1 : FA port map (
				A 	=> A(1),
				B 	=>  B(1),
				CI => sCO(0),
				S  => S(1),
				CO => sCO(1)
	);
	
	FA2 : FA port map (
				A 	=> A(2),
				B 	=>  B(2),
				CI => sCO(1),
				S  => S(2),
				CO  => CO
	);	
	
end architecture;