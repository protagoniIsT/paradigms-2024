:- load_library('alice.tuprolog.lib.DCGLibrary').

nvar(V, _) :- var(V).
nvar(V, T) :- nonvar(V), call(T).

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

side_wspace --> [].
side_wspace --> [' '], side_wspace.
internal_wspace --> [' '], side_wspace.

find([], _) --> [].
find([H | T], Symbols) --> { member(H, Symbols) }, [H], find(T, Symbols).

operator(OpSymbol) --> { atom_chars(OpSymbol, C) }, C.

op_p(op_add) --> operator("+").
op_p(op_subtract) --> operator("-").
op_p(op_multiply) --> operator("*").
op_p(op_divide) --> operator("/").
op_p(op_negate) --> operator("negate").
op_p(op_cube) --> operator("cube").
op_p(op_cbrt) --> operator("cbrt").

calc(op_add, A, B, R) :- R is A + B.
calc(op_subtract, A, B, R) :- R is A - B.
calc(op_multiply, A, B, R) :- R is A * B.
calc(op_divide, A, B, R) :- (R is A / B).
calc(op_negate, A, R) :- R is -A.
calc(op_cube, A, R) :- R is A * A * A.
calc(op_cbrt, A, R) :-
    ( A < 0 ->
        AbsA is abs(A),
        R is - (AbsA ** (1/3));
        R is A ** (1/3)
    ).

evaluate(const(Val), Vars, Val).
evaluate(variable(Name), Varmap, Val) :- lookup(Name, Varmap, Val).
evaluate(operation(UnOp, A), Variables, Val) :-
	evaluate(A, Variables, AVal),
	calc(UnOp, AVal, Val).
evaluate(operation(BinOp, A, B), Vars, Val) :-
	evaluate(A, Vars, AVal),
	evaluate(B, Vars, BVal),
	calc(BinOp, AVal, BVal, Val).

build_expr(const(Val)) -->
    side_wspace,
    { nvar(Val, number_chars(Val, Chars)) },
    find(Chars, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-']),
    { Chars = [_ | _], \+ Chars = ['-'], number_chars(Val, Chars) },
    side_wspace.
build_expr(variable(Name)) -->
	side_wspace,
	{ nvar(Name, atom_chars(Name, Chars)) },
	find(Chars, ['x', 'y', 'z', 'X', 'Y', 'Z']),
	{ Chars = [_ | _], atom_chars(Name, Chars) },
	side_wspace.
build_expr(operation(UnOp, A)) -->
    side_wspace, ['('],
    side_wspace, op_p(UnOp), internal_wspace, build_expr(A),
    side_wspace, [')'],
    side_wspace.
build_expr(operation(BinOp, A, B)) -->
    side_wspace, ['('], side_wspace,
    op_p(BinOp), internal_wspace, build_expr(A), internal_wspace, build_expr(B),
    side_wspace, [')'], side_wspace.

prefix_str(E, A) :- ground(E), phrase(build_expr(E), C), atom_chars(A, C), !.
prefix_str(E, A) :- atom(A), atom_chars(A, C), phrase(build_expr(E), C), !.
