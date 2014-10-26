require 'rubygems'
require 'nokogiri' 
require 'open-uri'    
vec = []
vec_2 = []
c = 0
i = 0
PAGE_URL = "http://monitor.planet-lab.eu/monitor/node"
page = Nokogiri::HTML(open(PAGE_URL))

page.css('#nodelist > tbody > tr').each do |el|
	if el.text.split(" ")[3] == "good" || el.text.split(" ")[3] == "online"
		vec << el.text.split(" ")[2]
		c = c + 1
	end
end

puts "#{c} nodes online."
puts "Number of nodes:"
nodes = gets.chomp
raise TypeError, 'Invalid number of nodes' if nodes.to_i > c

nodes.to_i.times.map { Random.rand(c) }.each { |el| vec_2 << vec[el] }

File.open('nodes.txt', 'w') do |f|
	if nodes.to_i == c
		vec.each { |ch| f.write("#{ch}\n") }
		puts "#{vec.size} added to nodes.txt"
	else
		vec_2.uniq.each { |ch| f.write("#{ch}\n") }
		puts "#{vec_2.uniq.size} added to nodes.txt"
	end
end

