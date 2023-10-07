LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY register4_tb is
end register4_tb;

architecture teste of register4_tb is
	component register4 is
	PORT( 	I 	: in std_logic_vector(3 downto 0);
			EN 	: in STD_LOGIC;
			RST : in std_logic;
			CLK : IN STD_LOGIC;
			Q 	: out std_logic_vector(3 downto 0)
			);	
	end component;
	constant MCLK_PERIOD: time:= 20 ns;
	constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;


	signal I_tb		: std_logic_vector(3 downto 0); 
	signal EN_tb	: std_logic;
	signal RST_tb	: std_logic;
	signal CLK_tb	: std_logic; 
	signal Q_tb		: std_logic_vector(3 downto 0); 



	Begin 
	UUT: register4 
			port map ( 
				clk => clk_tb,
				RST => rst_tb,
				I 	=> I_tb,
				EN 	=> en_tb,
				Q 	=> Q_tb
			);

		clk_gen : process
			Begin
				clk_tb <= '0';
				wait for MCLK_HALF_PERIOD;
				clk_tb <= '1';
				wait for MCLK_HALF_PERIOD;
			end process;
		stimulus: process

		begin
		--reset
		    rst_tb <= '1';
			WAIT FOR MCLK_PERIOD;
	       rst_tb <= '0';
			EN_tb <= '0';
		--Begin
	        WAIT FOR MCLK_PERIOD*2;
	        I_tb <= "1111";
	        WAIT FOR MCLK_PERIOD*2;
	        EN_tb <= '1';
	        WAIT FOR MCLK_PERIOD*2;
			  EN_tb <= '0';
	        WAIT FOR MCLK_PERIOD*2;
	        I_tb <= "0000";
	        WAIT FOR MCLK_PERIOD*2;
	        EN_tb <= '1';
	        WAIT FOR MCLK_PERIOD*2;
			En_tb <= '0';
			wait;
		end process;
end teste;