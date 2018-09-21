public class Expression
{    
    /* String value:
     * The current value of the expression, which can be changed by calling
     * Expression.reduce(). This method either
     * (1) applies one or more logical operations, reduce value's length, or
     * (2) replaces value with an error message if it contains an invalid
     * character (a character not included in TOKENS) or is not well-formed, or
     * (3) does not change value if value is a single, irreducible character or
     * is an error message. */
    private String value;
    
    /* boolean reducible:
     * Whether value can be reduced by applying logical operations. reducible
     * is true unless value is a single, irreducible character or value is an
     * error message. */
    private boolean reducible;
    
    
    @Override
    public String toString()
    {
        return this.value;
    }
    
    public boolean canBeReduced()
    {
        return this.reducible;
    }
    
    public Expression( String str )
    {
        this.value = str.replaceAll( " ", "" )
                        .replaceAll( "<->", "↔" )
                        .replaceAll( "->", "→" )
                        .replace( 't', 'T' )
                        .replace( 'f', 'F' );
        this.reducible = true;
        for( int i = this.value.length() - 1; i >= 0; --i )
        {
            String ithChar = this.value.substring( i, i+1 );
            if( !"TF?!&|↔→()".contains( ithChar ) )
            {
                this.value += "\n  Error: Invalid char " + ithChar;
                this.reducible = false;
                return;
            }
        }
    }
    
    private static boolean isTruthValue( char c )
    {
        return ( c=='T' || c=='F' || c=='?' );
    }
    
    private static boolean isAndOrOrIff( char c )
    {
        return ( c=='&' || c=='|' || c=='↔' );
    }
    
    private static char not( char c )
    {
        if( c == 'T' )
            return 'F';
        if( c == 'F' )
            return 'T';
        return c;
        // No error checking
    }
    
    private static char and( char a, char b )
    {
        if( a == 'F' || b == 'F' )
            return 'F';
        if( a == 'T' && b == 'T' )
            return 'T';
        return '?';
        // No error checking here either
    }
    
    private static char or( char a, char b )
    {
        if( a == 'T' || b == 'T' )
            return 'T';
        if( a == 'F' && b == 'F' )
            return 'F';
        return '?';
        // No error checking here either
    }
    
    private static char iff( char a, char b )
    {
        if( a == '?' || b == '?' )
            return '?';
        if( a == b )
            return 'T';
        return 'F';
        // No error checking here either
    }
    
    /* char imp( char a, char b):
     * Performs the operation of implication. In this program, I have chosen to
     * define implication such that T→T=T, T→F=F, and a→b=? for ANY other
     * operations besides those two. */
    private static char imp( char a, char b )
    {
        if( a == 'T' )
            return b;
        return '?';
        // No error checking
    }
    
    public void reduce()
    {
        // First, initial setup occurs.
        if( !this.reducible )
            return;
        boolean changed = false;
        int   i = 0;
        int lim = this.value.length() - 1;// exclusive max of i
        
        // Second, the Not operator is applied wherever possible.
        while( i < lim )
        {
            if( this.value.charAt(i) == '!'
                && isTruthValue( this.value.charAt( i+1 ) ) )
            {
                this.value = this.value.substring( 0, i ) +
                             not( this.value.charAt( i+1 ) ) +
                             this.value.substring( i+2 );
                changed = true;
                --lim;
            }
            else
                ++i;
        }
        
        /* Third, if the Not operator was not applied, the first occurring
         * binary operator other than implication (And, Or, or Iff) is applied,
         * if it exists. */
        if( !changed )
        {
            i   = 0;
            lim = this.value.length() - 2;
            char c0, c1, c2;// cx = the (i+x)th char
            while( !changed && i < lim )
            {
                c0 = this.value.charAt(  i  );
                c1 = this.value.charAt( i+1 );
                c2 = this.value.charAt( i+2 );
                if( isTruthValue(c0) && isAndOrOrIff(c1) && isTruthValue(c2) )
                {
                    char replacement;
                    switch( this.value.charAt( i+1 ) )
                    {
                        case '&':  replacement = and( c0, c2 ); break;
                        case '|':  replacement = or(  c0, c2 ); break;
                        case '↔': replacement = iff( c0, c2 ); break;
                        // Default given for compiler only, will not execute
                        default: return;
                    }
                    this.value = this.value.substring( 0, i ) + replacement +
                                 this.value.substring( i+3 );
                    changed = true;
                }
                else
                    ++i;
            }
        }
        
        /* Fourth, if no operator has yet been applied, the first occuring
         * implication operator is applied, if it exists. */
        if( !changed )
        {
            i   = 0;
            lim = this.value.length() - 2;
            char c0, c1, c2;// cx = the (i+x)th char
            while( !changed && i < lim )
            {
                c0 = this.value.charAt(  i  );
                c1 = this.value.charAt( i+1 );
                c2 = this.value.charAt( i+2 );
                if( isTruthValue(c0) && c1=='→' && isTruthValue(c2) )
                {
                    this.value = this.value.substring( 0, i ) + imp( c0, c2 ) +
                                 this.value.substring( i+3 );
                    changed = true;
                }
                else
                    ++i;
            }
        }
        
        /* Fifth, regardless of what happened elsewhere in the method,
         * parentheses around single truth values are removed. */
        i   = 0;
        lim = this.value.length() - 2;
        while( i < lim )
        {
            if( this.value.charAt(i) == '(' &&
                isTruthValue( this.value.charAt( i+1 ) ) &&
                this.value.charAt( i+2 ) == ')' )
            {
                this.value = this.value.substring( 0, i ) +
                             this.value.charAt( i+1 ) +
                             this.value.substring( i+3 );
                changed = true;
                lim -= 2;
            }
            else
                ++i;
        }
        
        /* Sixth, it is determined whether the expression can be reduced any
         * more and, if not, whether the expression has an error. */
        if( this.value.length() == 1 && isTruthValue( this.value.charAt(0) ) )
            this.reducible = false;
        else if( !changed )
        {
            /* The expression has not been reduced to a single truth value, but
             * it cannot be reduced thereto either, so it must have an error. */
            this.value = "Error: The expression \"" + this.value + "\" is not" +
                         " a valid expression and cannot be further reduced.";
            this.reducible = false;
        }
    }
}
