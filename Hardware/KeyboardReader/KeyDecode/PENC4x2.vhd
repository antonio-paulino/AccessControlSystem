LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Penc4x2 is
   port(	I: in std_logic_vector(3 downto 0);
			O : out STD_LOGIC_VECTOR(1 DOWNTO 0);
			GS: out STD_LOGIC
   );
end Penc4x2;

architecture arq_penc of Penc4x2 is
begin

   O(1) <= I(3) or I(2);
	O(0) <= (I(1) and not I(2)) or I(3);
	GS <= I(0) or I(1) or I(2) or I(3);
	
end arq_penc;