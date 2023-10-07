LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity OutputBuffer is
   port(
		D	 : in std_logic_vector(3 downto 0);
		clk : in std_logic;
		RST : in std_logic;
		Load: in std_logic;
		ACK : in std_logic;
		Obfree : out std_logic;
		Dval	: out std_logic;
		Q 		: out std_logic_vector(3 downto 0)
   );
end OutputBuffer;

architecture arq_buffer of OutputBuffer is

signal wReg : std_logic;

component register4 is
PORT( I  : in std_logic_vector(3 downto 0);
		EN : in STD_LOGIC;
		RST: in std_logic;
		CLK: IN STD_LOGIC;
		Q 	: out std_logic_vector(3 downto 0)
		);	
end component;
	
component OutBufferControl is
	port (
		clk : in std_logic;
		RST : in std_logic;
		Load: in std_logic;
		ACK : in std_logic;
		Obfree: out std_logic;	
		Dval: out std_logic;	
		wreg: out std_logic
	);
end component;


begin
	OutputReg : register4 port map(
		I   => D,
		EN  => '1',
		CLK => wReg,
		RST => RST,
		Q 	 => Q
	);
	
	BufferControl : OutBufferControl port map(
		clk  => clk,
		RST  => RST,
		Load => Load,
		ACK  => ACK,
		Obfree => Obfree,
		Dval => Dval,
		wreg => wReg
	);
	
end architecture;