// Compiled by ClojureScript 1.10.339 {}
goog.provide('cljs.repl');
goog.require('cljs.core');
goog.require('cljs.spec.alpha');
cljs.repl.print_doc = (function cljs$repl$print_doc(p__9092){
var map__9093 = p__9092;
var map__9093__$1 = ((((!((map__9093 == null)))?(((((map__9093.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__9093.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__9093):map__9093);
var m = map__9093__$1;
var n = cljs.core.get.call(null,map__9093__$1,new cljs.core.Keyword(null,"ns","ns",441598760));
var nm = cljs.core.get.call(null,map__9093__$1,new cljs.core.Keyword(null,"name","name",1843675177));
cljs.core.println.call(null,"-------------------------");

cljs.core.println.call(null,[cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var temp__5753__auto__ = new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(temp__5753__auto__)){
var ns = temp__5753__auto__;
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(ns),"/"].join('');
} else {
return null;
}
})()),cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Protocol");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m))){
var seq__9095_9117 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m));
var chunk__9096_9118 = null;
var count__9097_9119 = (0);
var i__9098_9120 = (0);
while(true){
if((i__9098_9120 < count__9097_9119)){
var f_9121 = cljs.core._nth.call(null,chunk__9096_9118,i__9098_9120);
cljs.core.println.call(null,"  ",f_9121);


var G__9122 = seq__9095_9117;
var G__9123 = chunk__9096_9118;
var G__9124 = count__9097_9119;
var G__9125 = (i__9098_9120 + (1));
seq__9095_9117 = G__9122;
chunk__9096_9118 = G__9123;
count__9097_9119 = G__9124;
i__9098_9120 = G__9125;
continue;
} else {
var temp__5753__auto___9126 = cljs.core.seq.call(null,seq__9095_9117);
if(temp__5753__auto___9126){
var seq__9095_9127__$1 = temp__5753__auto___9126;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__9095_9127__$1)){
var c__4351__auto___9128 = cljs.core.chunk_first.call(null,seq__9095_9127__$1);
var G__9129 = cljs.core.chunk_rest.call(null,seq__9095_9127__$1);
var G__9130 = c__4351__auto___9128;
var G__9131 = cljs.core.count.call(null,c__4351__auto___9128);
var G__9132 = (0);
seq__9095_9117 = G__9129;
chunk__9096_9118 = G__9130;
count__9097_9119 = G__9131;
i__9098_9120 = G__9132;
continue;
} else {
var f_9133 = cljs.core.first.call(null,seq__9095_9127__$1);
cljs.core.println.call(null,"  ",f_9133);


var G__9134 = cljs.core.next.call(null,seq__9095_9127__$1);
var G__9135 = null;
var G__9136 = (0);
var G__9137 = (0);
seq__9095_9117 = G__9134;
chunk__9096_9118 = G__9135;
count__9097_9119 = G__9136;
i__9098_9120 = G__9137;
continue;
}
} else {
}
}
break;
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m))){
var arglists_9138 = new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_((function (){var or__3949__auto__ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__3949__auto__)){
return or__3949__auto__;
} else {
return new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m);
}
})())){
cljs.core.prn.call(null,arglists_9138);
} else {
cljs.core.prn.call(null,((cljs.core._EQ_.call(null,new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.first.call(null,arglists_9138)))?cljs.core.second.call(null,arglists_9138):arglists_9138));
}
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"special-form","special-form",-1326536374).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Special Form");

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.contains_QMARK_.call(null,m,new cljs.core.Keyword(null,"url","url",276297046))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))){
return cljs.core.println.call(null,["\n  Please see http://clojure.org/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))].join(''));
} else {
return null;
}
} else {
return cljs.core.println.call(null,["\n  Please see http://clojure.org/special_forms#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join(''));
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"Macro");
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.call(null,"REPL Special Function");
} else {
}

cljs.core.println.call(null," ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
var seq__9099_9139 = cljs.core.seq.call(null,new cljs.core.Keyword(null,"methods","methods",453930866).cljs$core$IFn$_invoke$arity$1(m));
var chunk__9100_9140 = null;
var count__9101_9141 = (0);
var i__9102_9142 = (0);
while(true){
if((i__9102_9142 < count__9101_9141)){
var vec__9103_9143 = cljs.core._nth.call(null,chunk__9100_9140,i__9102_9142);
var name_9144 = cljs.core.nth.call(null,vec__9103_9143,(0),null);
var map__9106_9145 = cljs.core.nth.call(null,vec__9103_9143,(1),null);
var map__9106_9146__$1 = ((((!((map__9106_9145 == null)))?(((((map__9106_9145.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__9106_9145.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__9106_9145):map__9106_9145);
var doc_9147 = cljs.core.get.call(null,map__9106_9146__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_9148 = cljs.core.get.call(null,map__9106_9146__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name_9144);

cljs.core.println.call(null," ",arglists_9148);

if(cljs.core.truth_(doc_9147)){
cljs.core.println.call(null," ",doc_9147);
} else {
}


var G__9149 = seq__9099_9139;
var G__9150 = chunk__9100_9140;
var G__9151 = count__9101_9141;
var G__9152 = (i__9102_9142 + (1));
seq__9099_9139 = G__9149;
chunk__9100_9140 = G__9150;
count__9101_9141 = G__9151;
i__9102_9142 = G__9152;
continue;
} else {
var temp__5753__auto___9153 = cljs.core.seq.call(null,seq__9099_9139);
if(temp__5753__auto___9153){
var seq__9099_9154__$1 = temp__5753__auto___9153;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__9099_9154__$1)){
var c__4351__auto___9155 = cljs.core.chunk_first.call(null,seq__9099_9154__$1);
var G__9156 = cljs.core.chunk_rest.call(null,seq__9099_9154__$1);
var G__9157 = c__4351__auto___9155;
var G__9158 = cljs.core.count.call(null,c__4351__auto___9155);
var G__9159 = (0);
seq__9099_9139 = G__9156;
chunk__9100_9140 = G__9157;
count__9101_9141 = G__9158;
i__9102_9142 = G__9159;
continue;
} else {
var vec__9108_9160 = cljs.core.first.call(null,seq__9099_9154__$1);
var name_9161 = cljs.core.nth.call(null,vec__9108_9160,(0),null);
var map__9111_9162 = cljs.core.nth.call(null,vec__9108_9160,(1),null);
var map__9111_9163__$1 = ((((!((map__9111_9162 == null)))?(((((map__9111_9162.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__9111_9162.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__9111_9162):map__9111_9162);
var doc_9164 = cljs.core.get.call(null,map__9111_9163__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_9165 = cljs.core.get.call(null,map__9111_9163__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println.call(null);

cljs.core.println.call(null," ",name_9161);

cljs.core.println.call(null," ",arglists_9165);

if(cljs.core.truth_(doc_9164)){
cljs.core.println.call(null," ",doc_9164);
} else {
}


var G__9166 = cljs.core.next.call(null,seq__9099_9154__$1);
var G__9167 = null;
var G__9168 = (0);
var G__9169 = (0);
seq__9099_9139 = G__9166;
chunk__9100_9140 = G__9167;
count__9101_9141 = G__9168;
i__9102_9142 = G__9169;
continue;
}
} else {
}
}
break;
}
} else {
}

if(cljs.core.truth_(n)){
var temp__5753__auto__ = cljs.spec.alpha.get_spec.call(null,cljs.core.symbol.call(null,[cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.ns_name.call(null,n))].join(''),cljs.core.name.call(null,nm)));
if(cljs.core.truth_(temp__5753__auto__)){
var fnspec = temp__5753__auto__;
cljs.core.print.call(null,"Spec");

var seq__9113 = cljs.core.seq.call(null,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"args","args",1315556576),new cljs.core.Keyword(null,"ret","ret",-468222814),new cljs.core.Keyword(null,"fn","fn",-1175266204)], null));
var chunk__9114 = null;
var count__9115 = (0);
var i__9116 = (0);
while(true){
if((i__9116 < count__9115)){
var role = cljs.core._nth.call(null,chunk__9114,i__9116);
var temp__5753__auto___9170__$1 = cljs.core.get.call(null,fnspec,role);
if(cljs.core.truth_(temp__5753__auto___9170__$1)){
var spec_9171 = temp__5753__auto___9170__$1;
cljs.core.print.call(null,["\n ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name.call(null,role)),":"].join(''),cljs.spec.alpha.describe.call(null,spec_9171));
} else {
}


var G__9172 = seq__9113;
var G__9173 = chunk__9114;
var G__9174 = count__9115;
var G__9175 = (i__9116 + (1));
seq__9113 = G__9172;
chunk__9114 = G__9173;
count__9115 = G__9174;
i__9116 = G__9175;
continue;
} else {
var temp__5753__auto____$1 = cljs.core.seq.call(null,seq__9113);
if(temp__5753__auto____$1){
var seq__9113__$1 = temp__5753__auto____$1;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__9113__$1)){
var c__4351__auto__ = cljs.core.chunk_first.call(null,seq__9113__$1);
var G__9176 = cljs.core.chunk_rest.call(null,seq__9113__$1);
var G__9177 = c__4351__auto__;
var G__9178 = cljs.core.count.call(null,c__4351__auto__);
var G__9179 = (0);
seq__9113 = G__9176;
chunk__9114 = G__9177;
count__9115 = G__9178;
i__9116 = G__9179;
continue;
} else {
var role = cljs.core.first.call(null,seq__9113__$1);
var temp__5753__auto___9180__$2 = cljs.core.get.call(null,fnspec,role);
if(cljs.core.truth_(temp__5753__auto___9180__$2)){
var spec_9181 = temp__5753__auto___9180__$2;
cljs.core.print.call(null,["\n ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name.call(null,role)),":"].join(''),cljs.spec.alpha.describe.call(null,spec_9181));
} else {
}


var G__9182 = cljs.core.next.call(null,seq__9113__$1);
var G__9183 = null;
var G__9184 = (0);
var G__9185 = (0);
seq__9113 = G__9182;
chunk__9114 = G__9183;
count__9115 = G__9184;
i__9116 = G__9185;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
} else {
return null;
}
}
});

//# sourceMappingURL=repl.js.map
