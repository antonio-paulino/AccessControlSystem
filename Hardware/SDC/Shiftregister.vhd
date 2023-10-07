LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY Shiftregister is
PORT( I 		: in std_logic;
		EN 	: in STD_LOGIC;
		RST 	: in std_logic;
		CLK 	: IN STD_LOGIC;
		DATA	: out std_logic_vector(4 downto 0)
		);	
end Shiftregister;

ARCHITECTURE logicregister OF Shiftregister is
		
COMPONENT FFD
PORT(	CLK 	: in std_logic;
		RESET : in STD_LOGIC;
		SET 	: in std_logic;
		D 		: IN STD_LOGIC;
		EN 	: IN STD_LOGIC;
		Q 		: out std_logic
		);
end component; 

COMPONENT reg IS
generic(n : natural :=1);
PORT(	CLK : in std_logic;
		RESET : in STD_LOGIC;
		SET : in std_logic;
		D : IN STD_LOGIC_VECTOR(n-1 downto 0);
		EN : IN STD_LOGIC;
		Q : out STD_LOGIC_VECTOR(n-1 downto 0)
		);
END COMPONENT;


signal sdata, shift: std_logic_vector (4 downto 0);	

BEGIN

shift <= I & sdata(4 downto 1);

U1 : reg generic map(5) port map (CLK => CLK,
	EN => EN,
	D => shift,
	Q => sdata,
	RESET => RST,
	SET => '0');

--FF0 : FFD port map(
--	CLK => CLK,
--	EN => EN,
--	D => I,
--	Q => sdata(0),
--	RESET => RST,
--	SET => '0'
--	
--);

--FF1 : FFD port map(
--	CLK => CLK, 
--	EN => EN,
--	D => sdata(0),
--	Q => sdata(1),
--	RESET => RST, 
--	SET => '0'
--);

--FF2 : FFD port map(
--	CLK => CLK, 
--	EN => EN,
--	D => sdata(1),
--	Q => sdata(2),
--	RESET => RST,
--	SET => '0'
--);

--FF3 : FFD port map(
--	CLK => CLK, 
--	EN => EN,
--	D => sdata(2),
--	Q => sdata(3),
--	RESET => RST, 
--	SET => '0'
--);
--FF4 : FFD port map(
--	CLK => CLK, 
--	EN => EN,
--	D => sdata(3),
--	Q => sdata(4),
--	RESET => RST, 
--	SET => '0'
--);



DATA <= sdata;
END logicregister;