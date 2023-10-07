LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity mux2to1_3bit is
   port(A : in STD_LOGIC_VECTOR(2 downto 0);
      B 	: in STD_LOGIC_VECTOR(2 downto 0);
      R 	: out STD_LOGIC_VECTOR(2 downto 0);
		S 	: in std_logic
   );
end mux2to1_3bit;

architecture arq_mux of mux2to1_3bit is
begin
   R(0) <= (A(0) and  not S) or (B(0) and S);
	R(1) <= (A(1) and  not S) or (B(1) and S);
	R(2) <= (A(2) and  not S) or (B(2) and S);
end arq_mux;