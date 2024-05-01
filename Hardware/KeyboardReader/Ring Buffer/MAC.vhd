LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity MAC is
   port(
		clk 	: in std_logic;
		RST	: in std_logic;
		Addr 	: out std_logic_vector(2 downto 0);
		putget : in std_logic;
		incget : in std_logic;
		incput : in std_logic;
		full 	: out std_logic;
		empty : out std_logic
   );
end MAC;

architecture arq_mac of MAC is
signal writeAddr, readAddr : std_logic_vector(3 downto 0);
signal sread, swrite : std_logic_vector(2 downto 0);
signal sEq: std_logic;

	component mux2to1_3bit is
		port (
			A : in STD_LOGIC_VECTOR(2 downto 0);
			B : in STD_LOGIC_VECTOR(2 downto 0);
			R : out STD_LOGIC_VECTOR(2 downto 0);
			S : in std_logic
		);
	end component;
	
	component counterUp4 is
PORT( 
		RST : in STD_LOGIC;
		CE  : in std_logic;
		CLK : IN STD_LOGIC;
		Q 	 : out std_logic_vector(3 downto 0)
		);	
end component;


begin
	countRead : counterUp4 port map(
		RST => RST,
		CE => incget,
		clk => clk,
		Q => readAddr
	);
	
	countWrite : counterUp4 port map(
		RST => RST,
		CE =>  incput,
		clk => clk,
		Q => writeAddr
	);
	
	addrSel : mux2to1_3bit port map(
		A => sread,
		B => swrite,
		R => addr,
		S => putget
	);
	
 swrite <= writeAddr(2) & writeAddr(1) & writeAddr(0);
 sRead <= readAddr(2) & readAddr(1) & readAddr(0);
 sEq <= (readAddr(2) xnor writeAddr(2)) and (readAddr(1) xnor writeAddr(1)) and(readAddr(0) xnor writeAddr(0));
 full <= (writeAddr(3) xor readAddr(3)) and sEq;
 empty <= (writeAddr(3) xnor readAddr(3))  and sEq;


end architecture;