LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity Decoder is
   port(	
	S	: in STD_LOGIC_VECTOR(1 downto 0);
	I	: out STD_LOGIC_vector(3 downto 0)
);
end Decoder;

architecture arq_dec of Decoder is
begin
	I(0)<= not S(0) and not S(1);
	I(1)<= S(0) and not S(1);
	I(2)<= not S(0) and S(1);
	I(3)<= S(0) and S(1);
end arq_dec;
	
	
	