LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY counterUp4 is
PORT( 
		RST 	: in STD_LOGIC;
		CE 		: in std_logic;
		CLK 	: IN STD_LOGIC;
		Q 		: out std_logic_vector(3 downto 0)
		);	
end counterUp4;

ARCHITECTURE logiccount OF counterUp4 is

	COMPONENT register4
	PORT( 	I 		: in std_logic_vector(3 downto 0);
			EN 		: in STD_LOGIC;
			RST 	: in std_logic;
			CLK 	: IN STD_LOGIC;
			Q 		: out std_logic_vector(3 downto 0)
	);	
	end component; 	

	component Adder4
		 port (
			A 	: in std_logic_vector(3 downto 0);
			B	: in std_logic_vector(3 downto 0);
			CI 	: in std_logic;
			S 	: out std_logic_vector(3 downto 0);
			CO 	: out std_logic
	);
	end component;

	
	signal SADDER: std_logic_vector(3 downto 0);
	signal SREGISTER: std_logic_vector(3 downto 0);

	BEGIN

	 REGISTERL : register4 port map(
		I => SADDER,
		EN => CE,
		CLK => CLK,
		Q => SREGISTER,
		RST => RST
	);

	ADDSUB : Adder4 port map(
		A => SREGISTER,
		B => "0001",
		CI => '0',
		S => SADDER
	);
	
	Q <= SREGISTER;

END logiccount;