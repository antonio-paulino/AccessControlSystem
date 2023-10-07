LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity LCD_Dispatcher is
	port (
		clk : in std_logic;
		RST : in std_logic;
		Dval : in std_logic; 
		Din : in std_logic_vector ( 4 downto 0);
		WrL : out std_logic;
		Dout : out std_logic_vector (4 downto 0);
		done : out std_logic
	);
end LCD_Dispatcher;



architecture logic of LCD_Dispatcher is

component counterUp4 is
PORT( 
		RST : in STD_LOGIC;
		CE : in std_logic;
		CLK : IN STD_LOGIC;
		Q : out std_logic_vector(3 downto 0)
		);	
end component;


type STATE_TYPE is (STATE_WRITE, STATE_DONE, STATE_WAIT);

signal currentstate, nextstate: STATE_TYPE;

signal inccount: std_logic;
signal seq15 : std_logic;	
signal scount: std_logic_vector(3 downto 0);
signal sclr: std_logic;


begin

	counter: counterUp4 port map(
		clk => clk,
		CE => inccount,
		RST => sclr,
		Q => scount
		);

	currentstate <= STATE_WAIT when RST = '1' else nextstate when rising_edge(clk);
	
	GenerateNextState:
	process(currentstate, Dval, seq15)
		Begin 
		case currentstate is
			when STATE_WAIT    =>   if(Dval = '1') then
									 	   	nextstate <= STATE_WRITE;
									   	else
											   nextstate <= STATE_WAIT;
										   end if;
											
			when STATE_WRITE  =>  if(seq15 = '0') then
									 	   	nextstate <= STATE_WRITE;
									   	else
											   nextstate <= STATE_DONE;
										   end if;
											 
												
			WHEN STATE_DONE  =>   if(Dval = '1') then
									 	   	nextstate <= STATE_DONE;
									   	else
											   nextstate <= STATE_WAIT;
										   end if;
												
		
		end case;
	end process;
	sclr <= '1' when currentstate = STATE_WAIT else '0';
	seq15 <= scount(3) and scount(2) and scount(1) and scount(0);
	inccount <= '1' when currentstate = STATE_WRITE and seq15 = '0' else '0';
	done <= '1' when currentstate = STATE_DONE else '0';
	WrL <= '1' when currentstate = STATE_WRITE  else '0';
	Dout <= Din;
		
end logic;