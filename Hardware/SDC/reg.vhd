LIBRARY ieee;
USE ieee.std_logic_1164.all;
use IEEE.numeric_std.ALL;

ENTITY reg IS
generic(n : natural :=1);
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC_VECTOR(n-1 downto 0);
		EN : IN STD_LOGIC;
		Q : out STD_LOGIC_VECTOR(n-1 downto 0)
		);
END reg;

ARCHITECTURE logicFunction OF reg IS

BEGIN


Q <= (others=>'0') when RESET = '1' else (others=>'1') when SET = '1' else D WHEN rising_edge(clk) and EN = '1';


END LogicFunction;