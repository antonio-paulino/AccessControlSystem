LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity FA is
	port (
		A : in std_logic;
		B : in std_logic;
		CI : in std_logic;
		S : out std_logic;
		CO : out std_logic
	);
end FA;

architecture logic of FA is

	component HA is
		port (
			A : in std_logic;
			B : in std_logic;
			S : out std_logic;
			CO : out std_logic
		);
	end component;
	
	signal sSHA0, sCOHA0, sCOHA1 : std_logic;
begin

	HA0 : HA port map (
				A => A,
				B =>  B,
				S  => sSHA0,
				CO  => sCOHA0
			);
	
	HA1 : HA port map (
				A => CI,
				B =>  sSHA0,
				S  => S,
				CO  => sCOHA1
			);
			
	CO <= sCOHA0 or sCOHA1;
	
end architecture;