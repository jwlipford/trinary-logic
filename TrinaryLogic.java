/* TrinaryLogic presents a step-by-step reduction of an expression involving
 * 3-valued logic (logic with 3 possible truth values: True, False, and Maybe).
 * A string argument comes from the command line or the console. The following
 * ten tokens are valid for use in the string:
 *     T  t    True
 *     F  f    False
 *     ?       Maybe
 *     !       Not
 *     &       And
 *     |       Or (inclusive)
 *     ↔ <->  Equivalence, iff, if and only if
 *     →  ->  Implication
 *     (       Open parenthesis
 *     )       Close parenthesis
 */

public class TrinaryLogic
{
    public static void main( String[] args )
    {
        String arg;
        if( args.length > 0 )
            arg = args[0];
        else
        {
            System.out.print( "TrinaryLogic> " );
            arg = ( new java.util.Scanner( System.in ) ).nextLine();
        }
        Expression expression = new Expression( arg );
        while( expression.canBeReduced() )
        {
            System.out.println( "  " + expression.toString() );
            expression.reduce();
        }
        System.out.println( "  " + expression.toString() );
    }
}
// Total program length, including comments: 269 lines
