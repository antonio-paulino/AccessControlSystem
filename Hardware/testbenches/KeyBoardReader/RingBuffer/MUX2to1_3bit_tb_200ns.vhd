LIBRARY IEEE;
USE IEEE.std_logic_1164.all;

ENTITY mux2to1_3bit_tb IS
END mux2to1_3bit_tb;

ARCHITECTURE teste OF mux2to1_3bit_tb IS

    COMPONENT mux2to1_3bit IS
        PORT (
            A     : in std_logic_vector(2 downto 0);
            B     : in std_logic_vector(2 downto 0);
            S     : in std_logic;
            R     : out std_logic_vector(2 downto 0)
        );
    END COMPONENT;
	 
	 constant MCLK_PERIOD: time:= 20 ns;
	 constant MCLK_HALF_PERIOD: time := MCLK_PERIOD/2;

  
    signal A_tb       : std_logic_vector(2 downto 0);
    signal B_tb       : std_logic_vector(2 downto 0);
    signal S_tb       : std_logic;
    signal R_tb       : std_logic_vector(2 downto 0);


	 Begin	 
    UUT : mux2to1_3bit
        PORT MAP (
            A       => A_tb,
            B       => B_tb,
            S       => S_tb,
            R       => R_tb
        );

	stimulus: process
   Begin
       
        A_tb   <= "111";
        B_tb   <= "000";
        S_tb 	<= '0';
        WAIT FOR MCLK_PERIOD;
        S_tb 	<= '1';
		  WAIT FOR MCLK_PERIOD;
        A_tb   <= "101";
        B_tb   <= "010";
        S_tb 	<= '0';
        WAIT FOR MCLK_PERIOD;
        S_tb 	<= '1';
        WAIT FOR MCLK_PERIOD;
        A_tb   <= "001";
        B_tb   <= "100";
        S_tb 	<= '0';
        WAIT FOR MCLK_PERIOD;
        S_tb 	<= '1';
        WAIT FOR MCLK_PERIOD;
        A_tb   <= "110";
        B_tb   <= "001";
        S_tb 	<= '0';
        WAIT FOR MCLK_PERIOD;
        S_tb 	<= '1';
		  
	
        WAIT;
    END PROCESS;

END teste;